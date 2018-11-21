package tech.onder.modules;

import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.services.ChunkReportManagementService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ChunkServiceProvider implements Provider<ChunkReportManagementService> {


    private ChunkConverter chunkConverter;

    @Inject
    public ChunkServiceProvider(ChunkConverter chunkConverter) {
        this.chunkConverter = chunkConverter;
    }

    @Override
    public ChunkReportManagementService get() {
        return new ChunkReportManagementService(this.chunkConverter);
    }
}

