package tech.onder.consumer.services;


import com.google.common.util.concurrent.AtomicDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    /**
     * Limit for meters queue. 17280 items are enough for meter. But for supplier we need much more. Factually 17280*numbers of consumers
     */
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

    private final Map<Long, PeriodReport> consumptionChunkReportMap = new HashMap<>();

    private final ChunkConverter chunkConverter;

    private final MeterRepo meterRepo;
@Inject
    public ChunkReportManagementService(ChunkConverter chunkConverter, MeterRepo meterRepo) {
        this.chunkConverter = chunkConverter;
        this.meterRepo = meterRepo;
    }

    public void subscribe(String uuid) {
        ConcurrentLinkedQueue<ConsumptionChunkReport> map = new ConcurrentLinkedQueue<>();
        long periodStart = initWebsocketTime();
        this.periodStart.put(uuid, periodStart);
        List<ConsumptionChunkReport> cr = this.storageByMeters
                .values()
                .stream()
                .flatMap(cQueue -> new ArrayList<>(cQueue).stream())
                .filter(c -> this.chunkMark(c).equals(periodStart))
                .collect(Collectors.toList());
        map.addAll(cr);
        this.registeredQueues.put(uuid, map);


    }

    private Long chunkMark(ConsumptionChunkReport chunk) {
        return (chunk.getTime() / reportSegmentLength) * reportSegmentLength;
    }

    private Long initWebsocketTime() {
        if (this.reportEnd.get() != 0) {
            return reportEnd.get();
        }
        return previousMark();
    }

    private Long previousMark() {
        return (LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() / reportSegmentLength) * reportSegmentLength;
    }

    private Long reportBeginFromTime() {
        return this.reportBegin(previousMark());
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

    private PeriodReport empty(Long periodTime) {
        PeriodReport pr = new PeriodReport();
        pr.setPrice(BigInteger.ZERO);
        pr.setConsumption(0.0);
        pr.setTime(periodTime);
        return pr;
    }

    private PeriodReport summarize(Long periodTime, ThreadLocal<BigInteger> lastPrice, List<ConsumptionChunkReport> rep) {
        Double consumption = rep.stream().mapToDouble(ConsumptionChunkReport::getPurchaseWh).sum();
        BigInteger tokens = rep.stream()
                .map(ConsumptionChunkReport::getPurchaseCost)
                .reduce((a, b) -> a.add(b))
                .orElse(BigInteger.ZERO);
        PeriodReport pr = new PeriodReport();
        BigInteger price = ChunkConverter.calculatePrice(tokens, consumption);
        if(!price.equals(BigInteger.ZERO)){
            lastPrice.set(price);
        }else{
            price = lastPrice.get();
        }
        pr.setPrice(price);
        pr.setConsumption(consumption);
        pr.setTime(periodTime);
        return pr;
    }

    public List<MeterReportDTO> meterReportDTOS() {
        Long beginOfDay = reportBeginFromTime();
        return meterReportDTOS(beginOfDay);
    }

    public List<MeterReportDTO> meterReportDTOS(Long from) {
        return meterRepo.all()
                .parallelStream()
                .map(m -> {
                    ConcurrentLinkedDeque<ConsumptionChunkReport> chunks = this.storageByMeters.get(m.getUuid());
                    if (chunks == null) {
                        return chunkConverter.toMeterDTO(m);
                    }
                    return
                            chunkConverter.toMeterDTO(calculateConsumptionReports(from, m.getUuid(), chunks));
                }).collect(Collectors.toList());
//        return this.storageByMeters
//                .entrySet()
//                .stream()
//                .map(q -> calculateConsumptionReports(from, q.getKey(), q.getValue()))
//                .map(chunkConverter::toMeterDTO)
//                .collect(Collectors.toList());
    }


    private ConsumptionChunkReport calculateConsumptionReports(Long from, String uuid, ConcurrentLinkedDeque<ConsumptionChunkReport> cQueue) {
        return new ArrayList<>(cQueue)
                .stream()
                .filter(c -> this.chunkMark(c) >= from)
                .reduce(this.createEmpty(uuid), this::sum);
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

    public WebsocketDTO websocketOutput(String uuid) {

        ConcurrentLinkedQueue<ConsumptionChunkReport> queue = this.registeredQueues.get(uuid);
        List<ConsumptionChunkReport> parts = new ArrayList<>(queue);
        Long currentMark = this.periodStart.get(uuid);
        Long expectedMark = previousMark();
        if (expectedMark > currentMark) {
            this.periodStart.put(uuid, expectedMark);
            queue.clear();
        }
        PeriodReport pr = summarize(expectedMark,ThreadLocal.withInitial(()->BigInteger.ZERO), parts);
        Long startOfDay = reportBegin(expectedMark);
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
        registeredQueues.forEach((k, v) -> {
            if (this.periodStart.get(k) < this.chunkMark(chunk)) {
                v.clear();
                this.periodStart.put(k, this.chunkMark(chunk));
            }
            if(this.periodStart.get(k)>this.chunkMark(chunk)){
                v.add(chunk);
            }
        });
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
        Long expReportEnd = previousMark();
        if (this.reportEnd.get() < expReportEnd) {
            synchronized (consumptionChunkReportMap) {
                if (this.reportEnd.get() < expReportEnd) {
                    this.consumptionChunkReportMap.clear();
                    this.consumptionChunkReportMap.putAll(this.periodReport(expReportEnd));
                    this.reportEnd.set(expReportEnd);
                }
            }
        }
        return this.consumptionChunkReportMap;
    }


    public void unsubscribe(String id) {
        synchronized (this.registeredQueues) {
            if (this.registeredQueues.contains(id)) {
                this.registeredQueues.remove(id);
                this.periodStart.remove(id);
            }
        }
    }

    public List<ConsumptionChunkReport> getMeters() {
        return this.storageByMeters.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
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
    public Map<Long, PeriodReport> periodReport(Long reportEnd) {

        List<Long> timeMarks = IntStream.range(0, numberOfSegments).mapToObj(i -> reportEnd - i * reportSegmentLength)
                .collect(Collectors.toList());
        ThreadLocal<BigInteger> price = ThreadLocal.withInitial(()->BigInteger.ZERO);
        Map<Long, PeriodReport> periods = this.storageByMeters
                .values()
                .stream()
                .flatMap(q -> Arrays.stream(q.toArray(new ConsumptionChunkReport[]{})))
                .collect(Collectors.groupingBy(e -> this.groupFunction(e, timeMarks)))
                .entrySet()
                .stream()
                .map(e -> this.summarize(e.getKey(), price, e.getValue()))
                .collect(Collectors.groupingBy(PeriodReport::getTime, this.singletonCollector()));
        periods.remove(0L);
        timeMarks.forEach(m -> {
            if (!periods.containsKey(m)) {
                periods.put(m, empty(m));
            }
        });

        return periods;
    }


    private Long reportBegin(Long reportEnd) {
        return reportEnd - numberOfSegments * reportSegmentLength;
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
            if (this.chunkMark(cr).equals(mark)) {
                return this.chunkMark(cr);
            }
        }
        return 0L;
    }
}
