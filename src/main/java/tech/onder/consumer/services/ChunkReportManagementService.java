package tech.onder.consumer.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Singleton
public class ChunkReportManagementService {

    Logger logger = LoggerFactory.getLogger(ChunkReportManagementService.class);

    AtomicLong calculationTime = new AtomicLong(0);

    private final Integer itemsLimit = 17280;

    private final Integer timeStorageItems = 144;

    private final Integer intervalLenght = 600;

    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<ConsumptionChunkReport>> storageByMeters = new ConcurrentHashMap<>();

    /**
     * 144 elem queue
     */
    private final ConcurrentLinkedDeque<PeriodReport> calculatedSegmentsStorage = new ConcurrentLinkedDeque<>();

    private final ConcurrentLinkedDeque<ConsumptionChunkReport> lastPart = new ConcurrentLinkedDeque<>();

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<ConsumptionChunkReport>> registeredQueues = new ConcurrentHashMap<>();

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
        ConcurrentLinkedQueue<ConsumptionChunkReport> map = new ConcurrentLinkedQueue<>();
        map.addAll(lastPart);
        this.registeredQueues.put(uuid, map);
    }

    //1 текущие счетчики - напрямую из очереди
    // отрезки
    private void addToSuplierQueue(ConsumptionChunkReport chunkReport) {
        String uuid = chunkReport.getUuid();
        if (!storageByMeters.containsKey(uuid)) {
            synchronized (storageByMeters) {
                if (!storageByMeters.containsKey(uuid)) {
                    ConcurrentLinkedDeque<ConsumptionChunkReport> queue = new ConcurrentLinkedDeque<>();
                    storageByMeters.put(uuid, queue);
                    if (chunkReport.getTime() > queueBegin.get()) {
                        queueBegin.set(chunkReport.getTime());
                    }
                }
            }
        }
        ConcurrentLinkedDeque<ConsumptionChunkReport> queue = storageByMeters.get(uuid);
        if (queue.size() > 0 && queue.size() < 5) {
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
        return Optional.of(storageByMeters.get(uuid))
                .map(q -> (List<ConsumptionChunkReport>) new ArrayList<>(q))
                .orElse(Collections.emptyList());
    }


    private void addToSegmentStorage(ConcurrentLinkedDeque<ConsumptionChunkReport> queue) {
        if (calculatedSegmentsStorage.size() == timeStorageItems) {
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
        return this.storageByMeters
                .entrySet()
                .stream()
                .map(q -> calculateConsumptionReports(q.getKey(), q.getValue()))
                .map(chunkConverter::toMeterDTO)
                .collect(Collectors.toList());
    }

    private ConsumptionChunkReport calculateConsumptionReports(String uuid, ConcurrentLinkedDeque<ConsumptionChunkReport> cQueue) {
        return new ArrayList<>(cQueue).stream()
                .reduce(createEmpty(uuid), this::sum);

    }

    private ConsumptionChunkReport createEmpty(String mId) {
        ConsumptionChunkReport cr = new ConsumptionChunkReport();
        cr.setUuid(mId);
        cr.setPurchaseCost(BigInteger.ZERO);
        cr.setSaleCost(BigInteger.ZERO);
        cr.setPrice(BigInteger.ZERO);
        cr.setPurchaseWh(0.0);
        cr.setSaleWh(0.0);
        return cr;

    }

    public WebsocketDTO calculate(String uuid) {

        List<ConsumptionChunkReport> parts;
        synchronized (this.registeredQueues) {
            parts = new ArrayList<>(this.registeredQueues.get(uuid));
           // this.registeredQueues.get(uuid).clear();
        }
        PeriodReport pr = summarize(parts);
        return chunkConverter.toWebsocketDTO(pr, this.meterReportDTOS());
    }


    public ConsumptionChunkReport sum(ConsumptionChunkReport to, ConsumptionChunkReport add) {
        to.setSaleCost(to.getSaleCost().add(add.getSaleCost()));
        to.setPurchaseCost(to.getPurchaseCost().add(add.getPurchaseCost()));
        to.setSaleWh(to.getSaleWh() + add.getSaleWh());
        to.setPurchaseWh(to.getPurchaseWh() + add.getPurchaseWh());

        if (to.getTime()==null || to.getTime() < add.getTime()) {
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
        if (tempBegin.get() != 0 && chunk.getTime() >= tempBegin.get() + intervalLenght) {
            synchronized (lastPart) {
                if (tempBegin.get() != 0 && chunk.getTime() >= tempBegin.get() + intervalLenght) {
                    addToSegmentStorage(lastPart);
                    lastPart.clear();
                    reportEnd.set(tempBegin.get() + intervalLenght);
                    tempBegin.set(tempBegin.get() + intervalLenght);
                    this.registeredQueues.forEach((k, v) -> v.clear());
                }
            }
        }
        addToSuplierQueue(chunk);
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


    public CompletionStage<Void> backup() {
        return CompletableFuture.runAsync(this::backupMeters)
                .thenRunAsync(this::backupSegments);

    }

    private void backupSegments() {
        try {
            File fw2 = new File("conf/segments-storage-back.json");
            List<PeriodReport> periodReports = new ArrayList<>(this.calculatedSegmentsStorage);
            String json = Json.toJson(periodReports).asText();
            FileUtils.writeStringToFile(fw2, json);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    private void backupMeters() {
        try {
            File fw = new File("conf/meters-storage-back.json");
            List<ConsumptionChunkReport> chunkToStore =
                    this.storageByMeters.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            String json = Json.toJson(chunkToStore).asText();
            FileUtils.writeStringToFile(fw, json);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public CompletableFuture<Void> restore() {

        ObjectMapper mapper = new ObjectMapper();
        CompletableFuture<Void> meters = CompletableFuture.runAsync(() -> loadMeterStorage(mapper));
        CompletableFuture<Void> segments = CompletableFuture.runAsync(() -> loadSegments(mapper));
        return CompletableFuture.allOf(segments, meters);

    }

    private void loadSegments(ObjectMapper mapper) {
        try {
            String segmentStorage = FileUtils.readFileToString(new File("conf/segments-storage-back.json"));
            List<PeriodReport> segments = mapper.readValue(segmentStorage, mapper.getTypeFactory().constructCollectionType(List.class, PeriodReport.class));
            synchronized (this.calculatedSegmentsStorage) {
                this.calculatedSegmentsStorage.clear();
                segments.stream().sorted(Comparator.comparing(PeriodReport::getConsumption))
                        .forEach(this.calculatedSegmentsStorage::push);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    private void loadMeterStorage(ObjectMapper mapper) {
        try {
            String meterStorage = FileUtils.readFileToString(new File("conf/meters-storage-back.json"));
            List<ConsumptionChunkReport> chunkReports = mapper.readValue(meterStorage, mapper.getTypeFactory().constructCollectionType(List.class, ConsumptionChunkReport.class));

            synchronized (this.storageByMeters) {
                this.storageByMeters.clear();
                chunkReports.stream().collect(Collectors.groupingBy(ConsumptionChunkReport::getUuid))
                        .forEach((k, v) -> this.storageByMeters.put(k, new ConcurrentLinkedDeque<>(v)));
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
