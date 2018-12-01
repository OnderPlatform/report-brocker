package tech.onder.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import play.libs.Json;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.consumer.services.ChunkReportManagementService;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class QueueService {


    private ChunkReportManagementService chunkReportManagementService;

    @Inject
    public QueueService(ChunkReportManagementService chunkReportManagementService) {
        this.chunkReportManagementService = chunkReportManagementService;
    }

    public CompletionStage<Void> backup() {
        return CompletableFuture.runAsync(this::backupMeters)
                .thenRunAsync(this::backupSegments);

    }


    private void backupSegments() {
        try {
            File fw2 = new File("conf/segments-storage-back.json");
            List<PeriodReport> periodReports = chunkReportManagementService.getSegments();
            String json = Json.toJson(periodReports).asText();
            FileUtils.writeStringToFile(fw2, json);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    private void backupMeters() {
        try {
            File fw = new File("conf/meters-storage-back.json");
            List<ConsumptionChunkReport> chunkToStore = chunkReportManagementService.getMeters();
            String json = Json.toJson(chunkToStore).asText();
            FileUtils.writeStringToFile(fw, json);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public CompletableFuture<Void> restore() {

        ObjectMapper mapper = new ObjectMapper();
        CompletableFuture<Void> meters = CompletableFuture.runAsync(() -> this.loadMeterStorage(mapper));
        CompletableFuture<Void> segments = CompletableFuture.runAsync(() -> loadSegments(mapper));
        return CompletableFuture.allOf(segments, meters);

    }

    private void loadSegments(ObjectMapper mapper) {
        try {
            String segmentStorage = FileUtils.readFileToString(new File("conf/segments-storage-back.json"));
            List<PeriodReport> segments = mapper.readValue(segmentStorage, mapper.getTypeFactory().constructCollectionType(List.class, PeriodReport.class));
            chunkReportManagementService.loadSegments(segments);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }

    private void loadMeterStorage(ObjectMapper mapper) {
        try {
            String meterStorage = FileUtils.readFileToString(new File("conf/meters-storage-back.json"));
            List<ConsumptionChunkReport> chunkReports = mapper.readValue(meterStorage, mapper.getTypeFactory().constructCollectionType(List.class, ConsumptionChunkReport.class));
            chunkReportManagementService.loadMeters(chunkReports);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
