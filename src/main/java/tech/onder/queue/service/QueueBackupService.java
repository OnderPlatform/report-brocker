package tech.onder.queue.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import play.libs.Json;
import tech.onder.queue.conf.ConsumptionQueueConf;
import tech.onder.queue.models.ConsumptionChunk;
import tech.onder.queue.IRestorableStorage;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class QueueBackupService {
    
    private IRestorableStorage iRestorableStorage;
    
    private ConsumptionQueueConf consumptionQueueConf;
    
    @Inject
    public QueueBackupService(IRestorableStorage iRestorableStorage, ConsumptionQueueConf aConsumptionQueueConf) {
        this.iRestorableStorage = iRestorableStorage;
        this.consumptionQueueConf = aConsumptionQueueConf;
    }
    
    public CompletionStage<Void> backup() {
        return CompletableFuture.runAsync(this::backupMeters);
        
    }
    
    private void backupMeters() {
        try {
            File fw = new File(consumptionQueueConf.getBackupFilename());
            List<ConsumptionChunk> chunkToStore = iRestorableStorage.dump();
            String json = Json.toJson(chunkToStore).asText();
            FileUtils.writeStringToFile(fw, json);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    public CompletableFuture<Void> restore() {
        return CompletableFuture.runAsync(this::loadMeters);
    }
    
    
    private void loadMeters() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String meterStorage = FileUtils.readFileToString(new File("consumptionQueueConf/meters-storage-back.json"));
            List<ConsumptionChunk> chunkReports = mapper.readValue(meterStorage, mapper.getTypeFactory()
                                                                                       .constructCollectionType(List.class, ConsumptionChunk.class));
            iRestorableStorage.loadAll(chunkReports);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
}
