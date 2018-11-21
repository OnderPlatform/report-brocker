package tech.onder.consumer.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Singleton
public class ChunkReportManagementService {

    Logger logger = LoggerFactory.getLogger(ChunkReportManagementService.class);

    AtomicLong calculationTime = new AtomicLong(0);

    private final Integer itemsLimit = 17280;

    private final Integer intervalLenght = 600;

    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<ConsumptionChunkReport>> storage = new ConcurrentHashMap<>();

    private final ConcurrentLinkedDeque<PeriodReport> calculatedSegmentsStorage = new ConcurrentLinkedDeque<>();

    private final ConcurrentLinkedDeque<ConsumptionChunkReport> lastPart = new ConcurrentLinkedDeque<>();

    private ConcurrentHashMap<String, BlockingQueue<ConsumptionChunkReport>> registeredQueues = new ConcurrentHashMap<>();

    private Map<Long, PeriodReport> consumptionChunkReportMap = new HashMap<>();

    private final AtomicLong reportEnd = new AtomicLong(0);

    private final AtomicLong queueBegin = new AtomicLong(0);

    private AtomicLong tempBegin = new AtomicLong(0);

    private final ChunkConverter chunkConverter;

    @Inject
    public ChunkReportManagementService(ChunkConverter chunkConverter) {
        this.chunkConverter = chunkConverter;
    }

    public void subscribe(String uuid) {
        BlockingQueue<ConsumptionChunkReport> map = new ArrayBlockingQueue<>(120);
        map.addAll(lastPart);
        this.registeredQueues.put(uuid, map);
    }

    //1 текущие счетчики - напрямую из очереди
    // отрезки
    private void addToSuplierQueue(ConsumptionChunkReport chunkReport) {
        String uuid = chunkReport.getUuid();
        if (!storage.containsKey(uuid)) {
            synchronized (storage) {
                if (!storage.containsKey(uuid)) {
                    ConcurrentLinkedDeque<ConsumptionChunkReport> queue = new ConcurrentLinkedDeque<>();
                    storage.put(uuid, queue);
                    if (chunkReport.getTime() > queueBegin.get()) {
                        queueBegin.set(chunkReport.getTime());
                    }
                }
            }
        }
        ConcurrentLinkedDeque<ConsumptionChunkReport> queue = storage.get(uuid);
        if (queue.size() > 0) {
            while (queue.peek().getTime() < queueBegin.get()) {
                logger.debug("removed " + queue.poll().toString());
            }
        }

        if (queue.size() == itemsLimit) {
            ConsumptionChunkReport chunk = queue.poll();
            logger.debug("removed " + chunk.toString());
        }
        queue.add(chunkReport);
    }

    public List<ConsumptionChunkReport> getForUUID(String uuid) {
        return Optional.of(storage.get(uuid))
                .map(q -> (List<ConsumptionChunkReport>) new ArrayList<>(q))
                .orElse(Collections.emptyList());
    }


    private void addToTimeStorage(ConcurrentLinkedDeque<ConsumptionChunkReport> queue) {
        if (calculatedSegmentsStorage.size() == itemsLimit) {
            calculatedSegmentsStorage.poll();
        }
        List<ConsumptionChunkReport> rep = new ArrayList<>(queue);
        PeriodReport pr = summarize(rep);

        calculatedSegmentsStorage.push(pr);
    }

    private PeriodReport summarize(List<ConsumptionChunkReport> rep) {
        Double consumption = rep.stream().mapToDouble(ConsumptionChunkReport::getPurchaseWh).sum();
        BigInteger tokens = rep.stream()
                .map(ConsumptionChunkReport::getPurchaseCost)
                .reduce((a, b) -> a.add(b))
                .orElse(BigInteger.ZERO);
        Long time = tempBegin.get();

        PeriodReport pr = new PeriodReport();

        pr.setPrice(ChunkConverter.calculatePrice(tokens, consumption));
        pr.setConsumption(consumption);
        pr.setTime(time);
        return pr;
    }

    public List<MeterReportDTO> meterReportDTOS() {
        return this.storage
                .entrySet()
                .stream()
                .map(q -> {
                    List<ConsumptionChunkReport> r = new ArrayList<>(q.getValue());
                    return r.stream().reduce((a, b) -> this.sum(a, b)).orElseGet(() -> {
                        ConsumptionChunkReport cr = new ConsumptionChunkReport();
                        cr.setUuid(q.getKey());
                        return cr;
                    });
                })
                .map(chunkConverter::toMeterDTO)
                .collect(Collectors.toList());
    }

    public WebsocketDTO calculate(String uuid) {
        BlockingQueue<ConsumptionChunkReport> queue = this.registeredQueues.get(uuid);
        List<ConsumptionChunkReport> parts = new ArrayList<>();
        try {
            while (true) {
                parts.add(queue.take());
            }
        } catch (InterruptedException ex) {

        }

        PeriodReport pr = summarize(parts);
        return chunkConverter.toWebsocketDTO(pr, this.meterReportDTOS());
    }


    public ConsumptionChunkReport sum(ConsumptionChunkReport to, ConsumptionChunkReport add) {
        to.setSaleCost(to.getSaleCost().add(add.getSaleCost()));
        to.setPurchaseCost(to.getPurchaseCost().add(add.getPurchaseCost()));
        to.setSaleWh(to.getSaleWh() + add.getSaleWh());
        to.setPurchaseWh(to.getPurchaseWh() + add.getPurchaseWh());

        if (to.getTime() < add.getTime()) {
            to.setTime(add.getTime());
        }
        return to;
    }

    public void add(ConsumptionChunkReport chunk) {
        registeredQueues.forEach((k, v) -> v.add(chunk));
        if (tempBegin.get() == 0) {
            synchronized (tempBegin) {

                tempBegin.set(chunk.getTime());
            }
        }
        if (tempBegin.get() != 0 && chunk.getTime() > tempBegin.get() + intervalLenght) {
            synchronized (lastPart) {
                if (tempBegin.get() != 0 && chunk.getTime() > tempBegin.get() + intervalLenght) {
                    addToTimeStorage(lastPart);
                    Iterator<ConsumptionChunkReport> iterator = lastPart.iterator();
                    while (iterator.hasNext()) {
                        ConsumptionChunkReport chunkReport = iterator.next();
                        addToSuplierQueue(chunkReport);
                    }
                    lastPart.clear();
                    reportEnd.set(tempBegin.get() + intervalLenght);
                    tempBegin.set(tempBegin.get() + intervalLenght);
                }
            }
        }
        lastPart.push(chunk);
    }

    public static <T> Collector<T, ?, T> singletonCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    public Map<Long, PeriodReport> partChunks() {
        if (this.calculationTime.get() < reportEnd.get()) {
            synchronized (consumptionChunkReportMap) {
                if (this.calculationTime.get() < reportEnd.get()) {
                    this.consumptionChunkReportMap = new ArrayList<>(this.calculatedSegmentsStorage)
                            .stream()
                            .collect(Collectors.groupingBy(PeriodReport::getTime, this.singletonCollector()));
                    this.calculationTime.set(reportEnd.get());
                }
            }
        }
        return this.consumptionChunkReportMap;
    }


    public void unsubscribe(String id) {
        synchronized (this.registeredQueues) {
            if (this.registeredQueues.contains(id)) {
                this.registeredQueues.remove(id);
            }
        }

    }
}
