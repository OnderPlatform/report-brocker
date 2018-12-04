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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Singleton
public class ChunkReportManagementService {

    private final Logger logger = LoggerFactory.getLogger(ChunkReportManagementService.class);

    private final AtomicLong reportEnd = new AtomicLong(0);

    private final AtomicLong lastTimestamp = new AtomicLong(0);

    /** Limit for meters queue. 17280 items are enough for meter. But for supplier we need much more. Factually 17280*numbers of consumers*/
    private final Integer itemsLimit = 172800;

    private final Integer numberOfSegments = 144;

    private final Integer reportSegmentLength = 600;

    private final Integer pushInterval = 5;


    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<ConsumptionChunkReport>> storageByMeters = new ConcurrentHashMap<>();

    /**
     * 144 elem queue
     */
    @Deprecated
    private final ConcurrentLinkedDeque<PeriodReport> calculatedSegmentsStorage = new ConcurrentLinkedDeque<>();

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<ConsumptionChunkReport>> registeredQueues = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Long> periodStart = new ConcurrentHashMap<>();

    private Map<Long, PeriodReport> consumptionChunkReportMap = new HashMap<>();

    private final ChunkConverter chunkConverter;

    @Inject
    public ChunkReportManagementService(ChunkConverter chunkConverter) {
        this.chunkConverter = chunkConverter;
    }

    public void subscribe(String uuid) {
        ConcurrentLinkedQueue<ConsumptionChunkReport> map = new ConcurrentLinkedQueue<>();
        this.registeredQueues.put(uuid, map);
        this.periodStart.put(uuid, expectedNextChunkTime());
    }


    private void addToMeterStorageQueue(ConsumptionChunkReport chunkReport) {
        String uuid = chunkReport.getUuid();
        if (!storageByMeters.containsKey(uuid)) {
            synchronized (storageByMeters) {
                if (!storageByMeters.containsKey(uuid)) {
                    ConcurrentLinkedDeque<ConsumptionChunkReport> queue = new ConcurrentLinkedDeque<>();
                    storageByMeters.put(uuid, queue);
                }
            }
        }
        ConcurrentLinkedDeque<ConsumptionChunkReport> queue = storageByMeters.get(uuid);
        if (queue.size() == itemsLimit) {
            ConsumptionChunkReport chunk = queue.poll();
            logger.debug("Removed item" + chunk.toString());
        }
        queue.add(chunkReport);
    }

    public List<ConsumptionChunkReport> getForUUID(String uuid) {
        return Optional.of(storageByMeters.get(uuid))
                .map(q -> (List<ConsumptionChunkReport>) new ArrayList<>(q))
                .orElse(Collections.emptyList());
    }

private PeriodReport empty(Long periodTime){
    PeriodReport pr = new PeriodReport();
    pr.setPrice(BigInteger.ZERO);
    pr.setConsumption(0.0);
    pr.setTime(periodTime);
    return pr;
}
    private PeriodReport summarize(Long periodTime, List<ConsumptionChunkReport> rep) {
        Double consumption = rep.stream().mapToDouble(ConsumptionChunkReport::getPurchaseWh).sum();
        BigInteger tokens = rep.stream()
                .map(ConsumptionChunkReport::getPurchaseCost)
                .reduce((a, b) -> a.add(b))
                .orElse(BigInteger.ZERO);
        PeriodReport pr = new PeriodReport();
        pr.setPrice(ChunkConverter.calculatePrice(tokens, consumption));
        pr.setConsumption(consumption);
        pr.setTime(periodTime);
        return pr;
    }

    public List<MeterReportDTO> meterReportDTOS() {
        Long beginOfDay = reportBegin();
        return meterReportDTOS(beginOfDay);
    }

    public List<MeterReportDTO> meterReportDTOS(Long from) {
        return this.storageByMeters
                .entrySet()
                .stream()
                .map(q -> calculateConsumptionReports(from, q.getValue())
                        .orElse(createEmpty(q.getKey())))
                .map(chunkConverter::toMeterDTO)
                .collect(Collectors.toList());
    }


    private Optional<ConsumptionChunkReport> calculateConsumptionReports(Long from, ConcurrentLinkedDeque<ConsumptionChunkReport> cQueue) {
        return new ArrayList<>(cQueue)
                .stream()
                .filter(c -> c.getTime() >= from)
                .reduce(this::sum);

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

        List<ConsumptionChunkReport> parts = new ArrayList<>(this.registeredQueues.get(uuid));
        Long periodTime = this.periodStart.get(uuid);
        if (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) >= periodTime + reportSegmentLength) {
            periodTime += reportSegmentLength;
            this.periodStart.put(uuid, periodTime);
        }
        PeriodReport pr = summarize(periodTime, parts);
        Long startOfDay = periodTime - numberOfSegments * reportSegmentLength;
        return chunkConverter.toWebsocketDTO(pr, this.meterReportDTOS(startOfDay));
    }


    public ConsumptionChunkReport sum(ConsumptionChunkReport to, ConsumptionChunkReport add) {
        to.setSaleCost(to.getSaleCost().add(add.getSaleCost()));
        to.setPurchaseCost(to.getPurchaseCost().add(add.getPurchaseCost()));
        to.setSaleWh(to.getSaleWh() + add.getSaleWh());
        to.setPurchaseWh(to.getPurchaseWh() + add.getPurchaseWh());

        if (to.getTime() == null || to.getTime() < add.getTime()) {
            to.setTime(add.getTime());
        }
        return to;
    }


    public void add(ConsumptionChunkReport chunk) {
        registeredQueues.forEach((k, v) -> v.add(chunk));
        if (lastTimestamp.get() < chunk.getTime()) {
            lastTimestamp.set(chunk.getTime());
        }
        addToMeterStorageQueue(chunk);
    }

    private <T> Collector<T, ?, T> singletonCollector() {
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
        Long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        if (this.reportEnd.get() + pushInterval < now) {
            synchronized (consumptionChunkReportMap) {
                if (this.reportEnd.get() + pushInterval < now) {
                    this.consumptionChunkReportMap = this.periodReport();
//
//                    this.reportEnd.addAndGet(5);
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


    public List<PeriodReport> getSegments() {
        return new ArrayList<>();
    }


    public List<ConsumptionChunkReport> getMeters() {
        return this.storageByMeters.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void loadSegments(List<PeriodReport> segments) {
        synchronized (this.calculatedSegmentsStorage) {
            this.calculatedSegmentsStorage.clear();
            segments.stream().sorted(Comparator.comparing(PeriodReport::getConsumption))
                    .forEach(this.calculatedSegmentsStorage::push);
        }
    }

    public void loadMeters(List<ConsumptionChunkReport> chunkReports) {
        synchronized (this.storageByMeters) {
            this.storageByMeters.clear();
            chunkReports.stream().collect(Collectors.groupingBy(ConsumptionChunkReport::getUuid))
                    .forEach((k, v) -> this.storageByMeters.put(k, new ConcurrentLinkedDeque<>(v)));
        }
    }

    /**
     * Returns map of {@see PeriodReport}s calculated for 10-minute segments of last 24 hours + 0-5 sec.
     *
     * @return
     */
    public Map<Long, PeriodReport> periodReport() {
        Long lastPeriodBegin = previousSegmentBegin();
        List<Long> timeMarks = IntStream.rangeClosed(1, numberOfSegments).mapToObj(i -> lastPeriodBegin - i * 600)
                .collect(Collectors.toList());

        Map<Long, PeriodReport> periods = this.storageByMeters
                .values()
                .stream()
                .flatMap(q -> Arrays.stream(q.toArray(new ConsumptionChunkReport[]{})))
                .collect(Collectors.groupingBy(e -> this.groupFunction(e, timeMarks)))
                .entrySet()
                .stream()
                .map(e -> this.summarize(e.getKey(), e.getValue()))
                .collect(Collectors.groupingBy(PeriodReport::getTime, this.singletonCollector()));
        periods.remove(0L);
        timeMarks.forEach(m->{
            if(!periods.containsKey(m)){
                periods.put(m, empty(m));
            }
        });
        reportEnd.set(lastPeriodBegin+reportSegmentLength);
        return periods;
    }

    private Long reportBegin() {
        return this.expectedNextChunkTime() - numberOfSegments * reportSegmentLength;
    }

    /**
     * Returns closest previous expecting time mark of meter`s push.
     *
     * @return
     */
    private Long previousSegmentBegin() {
        return expectedNextChunkTime() - reportSegmentLength;
    }

    private Long expectedNextChunkTime() {
        Long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long lastMeterPush = lastTimestamp.get();
        Long reportLastChunkTime = lastMeterPush;
        while (currentTime > reportLastChunkTime) {
            reportLastChunkTime += pushInterval;
        }
        return reportLastChunkTime;
    }


    public Long groupFunction(ConsumptionChunkReport cr, List<Long> timeMarks) {
        for (Long mark : timeMarks) {
            if (cr.getTime() >= mark) {
                return mark;
            }
        }
        return 0L;
    }
}
