package tech.onder.consumer.repository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.modules.AppConfig;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


public class ChunkReportRepo {

    Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private final Integer itemsLimit = 17280;
    /**
     * 12*60*24
     */
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<ConsumptionChunkReport>> storage;

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<ConsumptionChunkReport>> lastPart;

    public void add(String uuid, ConsumptionChunkReport chunkReport) {
        if (!storage.containsKey(uuid)) {
            synchronized (storage) {
                if (!storage.containsKey(uuid)) {
                    ConcurrentLinkedQueue<ConsumptionChunkReport> queue = new ConcurrentLinkedQueue<>();
                    storage.put(uuid, queue);
                }
            }
        }
        ConcurrentLinkedQueue<ConsumptionChunkReport> queue = storage.get(uuid);
        if (queue.size() == itemsLimit) {
            ConsumptionChunkReport chunk = queue.poll();
            logger.debug("removed " + chunk.toString());
        }
        queue.add(chunkReport);

    }

    public List<ConsumptionChunkReport> get(String uuid) {
        return Optional.of(storage.get(uuid))
                .map(q -> q.stream().collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
