package tech.onder.consumer.repository;


import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.modules.AppConfig;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class ChunkReportRepo {

    Logger logger = LoggerFactory.getLogger(AppConfig.class);

    AtomicLong calculateTime = new AtomicLong(0);

    private final Integer itemsLimit = 17280;
    /**
     * 12*60*24
     */
    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<ConsumptionChunkReport>> storage;

    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<ConsumptionChunkReport>> lastPart;

    private final Map<Long, ConsumptionChunkReport> consumptionChunkReportMap;


    public void add(String uuid, ConsumptionChunkReport chunkReport) {
        if (!storage.containsKey(uuid)) {
            synchronized (storage) {
                if (!storage.containsKey(uuid)) {
                    ConcurrentLinkedDeque<ConsumptionChunkReport> queue = new ConcurrentLinkedDeque<>();
                    storage.put(uuid, queue);
                }
            }
        }
        ConcurrentLinkedDeque<ConsumptionChunkReport> queue = storage.get(uuid);
        if (queue.size() == itemsLimit) {
            ConsumptionChunkReport chunk = queue.poll();
            logger.debug("removed " + chunk.toString());
        }
        queue.add(chunkReport);

    }

    public List<ConsumptionChunkReport> get(String uuid) {
        return Optional.of(storage.get(uuid))
                .map(q -> (List<ConsumptionChunkReport>) new ArrayList<>(q))
                .orElse(Collections.emptyList());
    }


    public Map<Long, ConsumptionChunkReport> partChunks() {
        Long requestTime =LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        if(this.calculateTime.get() - requestTime>5000){
            synchronized (consumptionChunkReportMap){
                if(this.calculateTime.get() - requestTime>5000){
                    this.consumptionChunkReportMap = this.storage
                            .values()
                            .stream()
                            .flatMap(d -> {
                                List<ConsumptionChunkReport> l = new ArrayList<>(d);
                                List<ConsumptionChunkReport> lr = Lists.reverse(l);
                                return Lists.partition(lr, 120)
                                        .stream()
                                        .map(l -> l.stream().reduce(totalConsumption())
                                        );
                            })
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.groupingBy(ConsumptionChunkReport::getTime,
                                    Collectors.reducing(this.totalConsumption())));
                    this.calculateTime.set(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                }
            }
        }
        return this.consumptionChunkReportMap;
    }

    private BinaryOperator<ConsumptionChunkReport> totalConsumption() {
        return (a, b) -> {
                    b.setSaleKwh(a.getSaleKwh() + b.getSaleKwh());
                    b.setPurchaseKwh(a.getPurchaseKwh() + b.getPurchaseKwh());
                    b.setPurchaseCost(a.getPurchaseCost() + b.getPurchaseCost());
                    b.setSaleCost(a.getSaleCost() + b.getSaleCost());
                    return b;
                };
    }


}
