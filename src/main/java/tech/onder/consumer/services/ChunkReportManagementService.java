package tech.onder.consumer.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.onder.common.StreamUtils;
import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.Conf;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.ConsumptionReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.consumer.models.WebsocketQueueItem;
import tech.onder.meters.repositories.MeterRepo;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static tech.onder.consumer.ChunkConverter.calculatePrice;

@Singleton
public class ChunkReportManagementService implements IWebsocketQueueManager {

    private final Logger logger = LoggerFactory.getLogger(ChunkReportManagementService.class);

    private final AtomicLong reportEnd = new AtomicLong(0);

    private final AtomicLong lastChunkTime = new AtomicLong(0);

    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<ConsumptionChunkReport>> storageByMeters = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<WebsocketQueueItem>> registeredQueues = new ConcurrentHashMap<>();

    private final Map<Long, PeriodReport> consumptionChunkReportMap = new HashMap<>();


    private final Conf conf;

    @Inject
    public ChunkReportManagementService(ChunkConverter chunkConverter, MeterRepo meterRepo) {
        this.chunkConverter = chunkConverter;
        this.meterRepo = meterRepo;
    }

    @Override
    public void subscribe(String aClientId, ConcurrentLinkedDeque<WebsocketQueueItem> aWebSocketQueue) {
        WebsocketQueueItem item = this.itemByTimemark(currentMark());
        aWebSocketQueue.add(item);
        this.registeredQueues.put(aClientId, aWebSocketQueue);
    }

    @Override
    public void unsubscribe(String aClientId) {
        synchronized (this.registeredQueues) {
            if (this.registeredQueues.contains(aClientId)) {
                this.registeredQueues.remove(aClientId);
            }
        }
    }

    private List<ConsumptionChunkReport> chunksForSegment(Long periodMark) {
        return this.storageByMeters
                .values()
                .stream()
                .flatMap(cQueue -> new ArrayList<>(cQueue).stream())
                .filter(c -> this.segmentMark(c).equals(periodMark))
                .collect(Collectors.toList());
    }

    /**
     * Calculates segment`s mark of {@link ConsumptionChunkReport}.
     *
     * @param chunk to calculate
     * @return
     */
    private Long segmentMark(ConsumptionChunkReport chunk) {
        return (chunk.getTime() / conf.getReportSegmentLength()) * conf.getReportSegmentLength();
    }

    /**
     * Calculates current segment for current time.
     *
     * @return
     */
    private Long currentMark() {
        return (LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() / conf.getReportSegmentLength()) * conf.getReportSegmentLength();
    }

    private Long reportBeginFromTime() {
        return this.reportBegin(currentMark());
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
        if (queue.size() == conf.getItemsLimit()) {
            ConsumptionChunkReport chunk = queue.poll();
            logger.debug("Removed item" + chunk.toString());
        }
        queue.add(chunkReport);
    }

    private PeriodReport empty(Long periodTime) {
        PeriodReport pr = new PeriodReport();
        pr.setPrice(BigInteger.ZERO);
        pr.setConsumption(0.0);
        pr.setTime(periodTime);
        return pr;
    }


    private PeriodReport summarize(Long aPeriodMark, List<ConsumptionChunkReport> aChunks) {
        Double consumption = aChunks.stream().mapToDouble(ConsumptionChunkReport::getPurchaseWh).sum();
        BigInteger tokens = aChunks.stream()
                .map(ConsumptionChunkReport::getPurchaseCost)
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);
        PeriodReport pr = new PeriodReport();
        BigInteger price = calculatePrice(tokens, consumption);
        pr.setPrice(price);
        pr.setConsumption(consumption);
        pr.setTime(aPeriodMark);
        return pr;
    }

    private PeriodReport summarize(Long aPeriodMark, ThreadLocal<BigInteger> aLastCalculatedPrice, List<ConsumptionChunkReport> aChunks) {
        PeriodReport pr = this.summarize(aPeriodMark, aChunks);
        if (!pr.getPrice().equals(BigInteger.ZERO)) {
            aLastCalculatedPrice.set(pr.getPrice());
            return pr;
        }
        BigInteger lastPrice = aLastCalculatedPrice.get();
        if (!lastPrice.equals(BigInteger.ZERO)) {
            pr.setPrice(lastPrice);
        }
        return pr;
    }

    public Map<String, ConsumptionReport> meterReports() {
        Long reportFirstSegmentMark = reportBeginFromTime();
        return meterReports(reportFirstSegmentMark);
    }

    public Map<String, ConsumptionReport> meterReports(Long aFromMark) {
        return this
                .storageByMeters
                .entrySet()
                .parallelStream()
                .map(e -> calculateConsumptionReports(aFromMark, e.getKey(), e.getValue()))
                .collect(Collectors.groupingBy(ConsumptionReport::getMeterId, StreamUtils.singletonCollector()));
    }

//    public List<MeterReportDTO> meterReports(Long from) {
//        return meterRepo.all()
//                .parallelStream()
//                .map(m -> {
//                    ConcurrentLinkedDeque<ConsumptionChunkReport> chunks = this.storageByMeters.get(m.getId());
//                    if (chunks == null) {
//                        return chunkConverter.toMeterDTO(m);
//                    }
//                    return
//                            chunkConverter.toMeterDTO(calculateConsumptionReports(from, m.getId(), chunks));
//                }).collect(Collectors.toList());
//    }


    private ConsumptionReport calculateConsumptionReports(Long from, String uuid, ConcurrentLinkedDeque<ConsumptionChunkReport> cQueue) {
        return cQueue.stream()
                .filter(c -> this.segmentMark(c) >= from)
                .reduce(this.createEmpty(uuid, from), this.reportAccumulator(), this.reportCombiner());
    }


    private ConsumptionReport createEmpty(String aMeterId, Long aFromMark) {
        ConsumptionReport report = new ConsumptionReport(aMeterId, aFromMark);
        report.setPurchaseCost(BigInteger.ZERO);
        report.setSaleCost(BigInteger.ZERO);
        report.setPurchaseWh(0.0);
        report.setSaleWh(0.0);
        report.setUpdateTime(0L);
        return report;
    }

    private ConsumptionChunkReport createEmpty(String mId) {
        ConsumptionChunkReport cr = new ConsumptionChunkReport();
        cr.setUuid(mId);
        cr.setPurchaseCost(BigInteger.ZERO);
        cr.setSaleCost(BigInteger.ZERO);
        cr.setPurchaseWh(0.0);
        cr.setSaleWh(0.0);
        return cr;

    }

    public BiFunction<ConsumptionReport, ConsumptionChunkReport, ConsumptionReport> reportAccumulator() {
        return (to, add) -> {
            to.setSaleCost(add.getSaleCost().add(to.getSaleCost()));
            to.setPurchaseCost(add.getPurchaseCost().add(to.getPurchaseCost()));
            to.setSaleWh(add.getSaleWh() + to.getSaleWh());
            to.setPurchaseWh(to.getPurchaseWh() + add.getPurchaseWh());

            if (to.getUpdateTime() < add.getTime()) {
                to.setUpdateTime(add.getTime());
            }
            to.setAveragePrice(calculatePrice(to));
            return to;
        };
    }

    public BinaryOperator<ConsumptionReport> reportCombiner() {
        return (a, b) -> {
            a.setSaleCost(b.getSaleCost().add(a.getSaleCost()));
            a.setPurchaseCost(b.getPurchaseCost().add(a.getPurchaseCost()));
            a.setSaleWh(b.getSaleWh() + a.getSaleWh());
            a.setPurchaseWh(a.getPurchaseWh() + b.getPurchaseWh());

            if (a.getUpdateTime() < b.getUpdateTime()) {
                a.setUpdateTime(b.getUpdateTime());
            }
            a.setAveragePrice(calculatePrice(a));
            return a;
        };
    }


    /**
     * Calculates elem for websocket queue for segment defined by timemark.
     *
     * @param timemark min timestamp of segment`s chunks
     * @return
     */
    private WebsocketQueueItem itemByTimemark(Long timemark) {
        List<ConsumptionChunkReport> parts = this.chunksForSegment(timemark);
        PeriodReport pr = summarize(timemark, parts);
        Long startOfDay = reportBegin(timemark);
        return new WebsocketQueueItem(pr, this.meterReports(startOfDay));
    }

    public void add(ConsumptionChunkReport chunk) {
        this.addToMeterStorageQueue(chunk);
        if (lastChunkTime.get() + conf.getPushInterval() <= chunk.getTime()) {
            lastChunkTime.set(chunk.getTime());
            WebsocketQueueItem item = this.itemByTimemark(this.segmentMark(chunk));
            this.registeredQueues.forEach((k, v) -> v.add(item));
        }
    }


    public Map<Long, PeriodReport> partChunks() {
        Long expReportEnd = currentMark();
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
    private Map<Long, PeriodReport> periodReport(Long reportEnd) {

        List<Long> timeMarks = IntStream.rangeClosed(1, conf.getNumberOfSegments()).mapToObj(i -> reportEnd - i * conf.getReportSegmentLength())
                .collect(Collectors.toList());
        ThreadLocal<BigInteger> price = ThreadLocal.withInitial(() -> BigInteger.ZERO);
        Map<Long, PeriodReport> periods = this.storageByMeters
                .values()
                .stream()
                .map(ArrayList::new)
                .flatMap(Collection::stream)
                .filter(c -> reportBegin(reportEnd) <= this.segmentMark(c))
                .collect(Collectors.groupingBy(e -> this.groupFunction(e, timeMarks)))
                .entrySet()
                .stream()
                .map(e -> this.summarize(e.getKey(), price, e.getValue()))
                .collect(Collectors.groupingBy(PeriodReport::getTime, StreamUtils.singletonCollector()));
        periods.remove(0L);
        timeMarks.forEach(m -> {
            if (!periods.containsKey(m)) {
                periods.put(m, empty(m));
            }
        });

        return periods;
    }


    private Long reportBegin(Long reportEnd) {
        return reportEnd - conf.getNumberOfSegments() * conf.getReportSegmentLength();
    }


    private Long groupFunction(ConsumptionChunkReport cr, List<Long> timeMarks) {
        for (Long mark : timeMarks) {
            if (this.segmentMark(cr).equals(mark)) {
                return this.segmentMark(cr);
            }
        }
        return 0L;
    }
}
