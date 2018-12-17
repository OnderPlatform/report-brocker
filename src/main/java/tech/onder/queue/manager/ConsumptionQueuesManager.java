package tech.onder.queue.manager;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.onder.collector.IConsumptionStorage;
import tech.onder.common.StreamUtils;
import tech.onder.queue.IRestorableStorage;
import tech.onder.queue.conf.ConsumptionQueueConf;
import tech.onder.queue.models.ConsumptionChunk;
import tech.onder.queue.models.WebsocketQueueItem;
import tech.onder.queue.models.dto.ConsumptionReportDTO;
import tech.onder.queue.models.dto.PeriodReportDTO;
import tech.onder.reports.IReportDataSupplier;
import tech.onder.reports.actors.IWebsocketQueuePublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Singleton
public class ConsumptionQueuesManager implements IWebsocketQueuePublisher,
                                                         IConsumptionStorage,
                                                         IReportDataSupplier,
                                                         IRestorableStorage<ConsumptionChunk> {
    
    private final Logger logger = LoggerFactory.getLogger(ConsumptionQueuesManager.class);
    
    private final AtomicLong reportEnd = new AtomicLong(0);
    
    private final AtomicLong lastChunkTime = new AtomicLong(0);
    
    private final ConcurrentHashMap<String, ArrayBlockingQueue<ConsumptionChunk>> storageByMeters = new ConcurrentHashMap<>();
    
    private final ConcurrentHashMap<String, ArrayBlockingQueue<WebsocketQueueItem>> registeredQueues = new ConcurrentHashMap<>();
    
    private final Map<Long, PeriodReportDTO> consumptionChunkReportMap = new HashMap<>();
    
    private final ConsumptionQueueConf consumptionQueueConf;
    
    
    @Inject
    public ConsumptionQueuesManager(ConsumptionQueueConf aConsumptionQueueConf) {
        this.consumptionQueueConf = aConsumptionQueueConf;
    }
    
    @Override
    public void subscribe(String aClientId, ArrayBlockingQueue<WebsocketQueueItem> aWebSocketQueue) {
        WebsocketQueueItem item = this.itemByTimemark(currentMark());
        aWebSocketQueue.add(item);
        this.registeredQueues.put(aClientId, aWebSocketQueue);
    }
    
    @Override
    public void feedWebsocketQueues() {
        if (!this.registeredQueues.isEmpty()) {
            WebsocketQueueItem item = this.itemByTimemark(this.currentMark());
            this.registeredQueues.forEach((k, v) -> this.forceAdd(item, v));
        }
    }
    
    @Override
    public void unsubscribe(String aClientId) {
        synchronized (this.registeredQueues) {
            if (this.registeredQueues.contains(aClientId)) {
                this.registeredQueues.remove(aClientId);
            }
        }
    }
    
    private List<ConsumptionChunk> chunksForSegment(List<ConsumptionChunk> aChunks, Long aPeriodMark) {
        return aChunks.stream()
                      .filter(c -> this.segmentMark(c).equals(aPeriodMark))
                      .collect(Collectors.toList());
    }
    
    /**
     * Calculates segment`s mark of {@link ConsumptionChunk}.
     *
     * @param aChunk to calculate
     * @return
     */
    private Long segmentMark(ConsumptionChunk aChunk) {
        return (aChunk.getTime() / consumptionQueueConf.getReportSegmentLength()) * consumptionQueueConf.getReportSegmentLength();
    }
    
    /**
     * Calculates current segment for current time.
     *
     * @return
     */
    private Long currentMark() {
        return (LocalDateTime.now()
                             .atZone(ZoneId.systemDefault())
                             .toEpochSecond() / consumptionQueueConf.getReportSegmentLength()) * consumptionQueueConf.getReportSegmentLength();
    }
    
    private Long reportFirstTimeMark() {
        return this.reportBegin(currentMark());
    }
    
    private void addToMeterStorage(ConsumptionChunk aChunk) {
        String meterId = aChunk.getUuid();
        if (!storageByMeters.containsKey(meterId)) {
            synchronized (storageByMeters) {
                if (!storageByMeters.containsKey(meterId)) {
                    ArrayBlockingQueue<ConsumptionChunk> queue = new ArrayBlockingQueue<>(consumptionQueueConf.getItemsLimit());
                    storageByMeters.put(meterId, queue);
                }
            }
        }
        ArrayBlockingQueue<ConsumptionChunk> queue = storageByMeters.get(meterId);
        this.forceAdd(aChunk, queue);
        
    }
    
    
    private PeriodReportDTO summarize(Long aPeriodMark, List<ConsumptionChunk> aChunks) {
        Double consumption = aChunks.stream().mapToDouble(ConsumptionChunk::getPurchaseWh).sum();
        BigInteger tokens = aChunks.stream()
                                   .map(ConsumptionChunk::getPurchaseCost)
                                   .reduce(BigInteger::add)
                                   .orElse(BigInteger.ZERO);
        PeriodReportDTO pr = new PeriodReportDTO();
        BigInteger price = calculatePrice(tokens, consumption);
        pr.setPrice(price);
        pr.setConsumption(consumption);
        pr.setTime(aPeriodMark);
        return pr;
    }
    
    private PeriodReportDTO summarize(Long aPeriodMark, ThreadLocal<BigInteger> aLastCalculatedPrice,
                                      List<ConsumptionChunk> aChunks) {
        PeriodReportDTO pr = this.summarize(aPeriodMark, aChunks);
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
    
    @Override
    public Map<String, ConsumptionReportDTO> meterConsumptionReports() {
        Long reportFirstSegmentMark = reportFirstTimeMark();
        return meterConsumptionReports(reportFirstSegmentMark);
    }
    
    private Map<String, ConsumptionReportDTO> meterConsumptionReports(Long aFromMark) {
        return this.storageByMeters.entrySet()
                                   .parallelStream()
                                   .map(e -> calculateConsumptionReports(aFromMark, e.getKey(), e.getValue()))
                                   .peek(this::fillAveragePrice)
                                   .collect(Collectors.groupingBy(ConsumptionReportDTO::getMeterId, StreamUtils.singletonCollector()));
    }
    
    private ConsumptionReportDTO calculateConsumptionReports(Long aFrom, String aMeterId, ArrayBlockingQueue<ConsumptionChunk> cQueue) {
        BiFunction<ConsumptionReportDTO, ConsumptionChunk, ConsumptionReportDTO> reportAccumulator =
                (to, add) -> {
                    to.setSaleCost(add.getSaleCost().add(to.getSaleCost()));
                    to.setPurchaseCost(add.getPurchaseCost().add(to.getPurchaseCost()));
                    to.setSaleWh(add.getSaleWh() + to.getSaleWh());
                    to.setPurchaseWh(to.getPurchaseWh() + add.getPurchaseWh());
                    if (to.getUpdateTime() < add.getTime()) {
                        to.setUpdateTime(add.getTime());
                    }
                    return to;
                };
        BinaryOperator<ConsumptionReportDTO> reportCombiner =
                (a, b) -> {
                    a.setSaleCost(b.getSaleCost().add(a.getSaleCost()));
                    a.setPurchaseCost(b.getPurchaseCost().add(a.getPurchaseCost()));
                    a.setSaleWh(b.getSaleWh() + a.getSaleWh());
                    a.setPurchaseWh(a.getPurchaseWh() + b.getPurchaseWh());
                    
                    if (a.getUpdateTime() < b.getUpdateTime()) {
                        a.setUpdateTime(b.getUpdateTime());
                    }
                    return a;
                };
        
        return cQueue.stream()
                     .filter(c -> this.segmentMark(c) >= aFrom)
                     .reduce(this.createEmpty(aMeterId, aFrom), reportAccumulator, reportCombiner);
    }
    
    
    private ConsumptionReportDTO createEmpty(String aMeterId, Long aFromMark) {
        ConsumptionReportDTO report = new ConsumptionReportDTO(aMeterId, aFromMark);
        report.setPurchaseCost(BigInteger.ZERO);
        report.setSaleCost(BigInteger.ZERO);
        report.setPurchaseWh(0.0);
        report.setSaleWh(0.0);
        report.setUpdateTime(0L);
        return report;
    }
    
    
    /**
     * Calculates elem for websocket queue for segment defined by timemark.
     *
     * @param aTimemark min timestamp of segment`s chunks
     * @return
     */
    private WebsocketQueueItem itemByTimemark(Long aTimemark) {
        List<ConsumptionChunk> chunksInQueue = valuesInQueue();
        List<ConsumptionChunk> parts = this.chunksForSegment(chunksInQueue, aTimemark);
        PeriodReportDTO pr = summarize(aTimemark, parts);
        Long startOfDay = reportBegin(aTimemark);
        return new WebsocketQueueItem(pr, this.meterConsumptionReports(startOfDay));
    }
    
    @Override
    public void push(ConsumptionChunk aChunk) {
        this.addToMeterStorage(aChunk);
//        if (lastChunkTime.get() + consumptionQueueConf.getPushInterval() <= aChunk.getTime()) {
//            WebsocketQueueItem item = this.itemByTimemark(this.segmentMark(aChunk));
//            this.registeredQueues.forEach((k, v) -> this.forceAdd(item, v));
//            lastChunkTime.set(aChunk.getTime());
//        }
    }
    
    /**
     * Todo
     *
     * @param aItem
     * @param aQueue
     */
    private <T> void forceAdd(T aItem, ArrayBlockingQueue<T> aQueue) {
        if (!aQueue.offer(aItem)) {
            aQueue.remove();
            aQueue.offer(aItem);
            logger.debug("Item has been removed " + aItem);
        }
    }
    
    @Override
    public Map<Long, PeriodReportDTO> periodReportBySegments() {
        Long expReportEnd = currentMark();
        if (this.reportEnd.get() < expReportEnd) {
            synchronized (consumptionChunkReportMap) {
                if (this.reportEnd.get() < expReportEnd) {
                    Map<Long, PeriodReportDTO> newPeriodReport = periodReport(expReportEnd);
                    this.consumptionChunkReportMap.clear();
                    this.consumptionChunkReportMap.putAll(newPeriodReport);
                    this.reportEnd.set(expReportEnd);
                }
            }
        }
        return this.consumptionChunkReportMap;
    }
    
    
    @Override
    public void loadAll(List<ConsumptionChunk> aConsumptionChunks) {
        synchronized (this.storageByMeters) {
            this.storageByMeters.clear();
            aConsumptionChunks.stream()
                              .collect(Collectors.groupingBy(ConsumptionChunk::getUuid))
                              .forEach((k, v) -> this.storageByMeters.put(k, new ArrayBlockingQueue<>(consumptionQueueConf
                                                                                                              .getItemsLimit())));
        }
    }
    
    /**
     * Returns map of {@see PeriodReportDTO}s calculated for 10-minute segments of last 24 hours + 0-5 sec.
     *
     * @return
     */
    private Map<Long, PeriodReportDTO> periodReport(Long aReportEndMark) {
        List<Long> timeMarks = IntStream.rangeClosed(1, consumptionQueueConf.getNumberOfSegments())
                                        .mapToObj(i -> aReportEndMark - i * consumptionQueueConf.getReportSegmentLength())
                                        .sorted()
                                        .collect(Collectors.toList());
        ThreadLocal<BigInteger> price = ThreadLocal.withInitial(() -> BigInteger.ZERO);
        List<ConsumptionChunk> chunksInQueue = this.valuesInQueue();
        
        Function<? super Long, ? extends PeriodReportDTO> periodReportByTimemark =
                t -> {
                    List<ConsumptionChunk> segmentChunks = this.chunksForSegment(chunksInQueue, t);
                    return this.summarize(t, price, segmentChunks);
                };
        return timeMarks.stream()
                        .collect(toMap(Function.identity(), periodReportByTimemark));
    }
    
    @NotNull
    private List<ConsumptionChunk> valuesInQueue() {
        return this.storageByMeters.values()
                                   .stream()
                                   .flatMap(Collection::stream)
                                   .collect(Collectors.toList());
    }
    
    
    private Long reportBegin(Long reportEnd) {
        return reportEnd - consumptionQueueConf.getNumberOfSegments() * consumptionQueueConf.getReportSegmentLength();
    }
    
    
    @Override
    public List<ConsumptionChunk> dump() {
        return valuesInQueue();
    }
    
    public BigInteger calculatePrice(BigInteger cost, Double kw) {
        if (new BigDecimal(kw).equals(BigDecimal.ZERO)) {
            return BigInteger.ZERO;
        }
        return new BigDecimal(cost).divide(new BigDecimal(kw), 2, RoundingMode.HALF_UP).toBigInteger();
    }
    
    public void fillAveragePrice(ConsumptionReportDTO consumptionReportDTO) {
        BigInteger castUp;
        Double castWh;
        if (!consumptionReportDTO.getPurchaseCost().equals(BigInteger.ZERO)) {
            castUp = consumptionReportDTO.getPurchaseCost();
            castWh = consumptionReportDTO.getPurchaseWh();
        } else {
            castUp = consumptionReportDTO.getSaleCost();
            castWh = consumptionReportDTO.getSaleWh();
        }
        consumptionReportDTO.setAveragePrice(calculatePrice(castUp, castWh));
    }
    
}
