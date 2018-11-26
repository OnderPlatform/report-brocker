package tech.onder.consumer.services;

import com.google.inject.Inject;
import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.models.ConsumptionChunkReport;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class CollectorService {

    private final ChunkReportManagementService reportRepo;

    @Inject
    public CollectorService(ChunkReportManagementService reportRepo) {
        this.reportRepo = reportRepo;
    }




}
