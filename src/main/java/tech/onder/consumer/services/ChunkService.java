package tech.onder.consumer.services;

import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.models.WebsocketQueueItem;
import tech.onder.meters.MeterService;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import java.util.List;

public class ChunkService {


    private final ChunkConverter chunkConverter;

    private final MeterService meterService;

    @Inject
    public ChunkService(ChunkConverter chunkConverter, MeterRepo meterRepo) {
        this.chunkConverter = chunkConverter;
        this.meterRepo = meterRepo;
    }

    public WebsocketDTO websocketOutput(WebsocketQueueItem websocketItem) {
        List<MeterReportDTO> meters = meterService.consumptionReport();

        return chunkConverter.toWebsocketDTO(websocketItem.getPeriodReport(), meters);
    }
}
