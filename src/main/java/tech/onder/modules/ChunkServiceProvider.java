package tech.onder.modules;

import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.consumer.services.IWebsocketQueueManager;
import tech.onder.meters.repositories.MeterRepo;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ChunkServiceProvider implements Provider<IWebsocketQueueManager> {


    private ChunkConverter chunkConverter;

    private MeterRepo meterRepo;

            @Inject
    public ChunkServiceProvider(ChunkConverter chunkConverter, MeterRepo meterRepo) {
        this.chunkConverter = chunkConverter;
        this.meterRepo = meterRepo;
    }

    @Override
    public IWebsocketQueueManager get() {
        return new ChunkReportManagementService(this.chunkConverter, meterRepo);
    }
}

