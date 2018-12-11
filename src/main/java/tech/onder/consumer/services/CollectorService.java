package tech.onder.consumer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.onder.consumer.converters.ConsumptionConverter;
import tech.onder.consumer.models.TransactionInputDTO;
import tech.onder.meters.MeterService;

import javax.inject.Inject;

import static play.libs.Json.toJson;

public class CollectorService {

    private final Logger logger = LoggerFactory.getLogger(CollectorService.class);

    private final ChunkReportManagementService chunkReportManagementService;

    private final ConsumptionConverter consumptionConverter;

    private final MeterService meterService;

    @Inject
    public CollectorService(ChunkReportManagementService chunkReportManagementService, ConsumptionConverter consumptionConverter, MeterService meterService) {
        this.chunkReportManagementService = chunkReportManagementService;
        this.consumptionConverter = consumptionConverter;
        this.meterService = meterService;
    }

    public void add(String uuid, TransactionInputDTO dto) {
        meterService.findOrThrow(uuid);
        consumptionConverter.toChunks(dto)
                .stream()
                .peek(v -> logger.trace(toJson(v).toString()))
                .forEach(chunkReportManagementService::add);
    }

}
