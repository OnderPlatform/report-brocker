package tech.onder.collector.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.onder.collector.models.dto.TransactionInputDTO;
import tech.onder.collector.IConsumptionStorage;
import tech.onder.collector.services.converters.ConsumptionConverter;
import tech.onder.meters.services.MeterService;

import javax.inject.Inject;

import static play.libs.Json.toJson;

public class CollectorService   {
    
    private final Logger logger = LoggerFactory.getLogger(CollectorService.class);
    
    private final IConsumptionStorage consumptionStorage;
    
    private final ConsumptionConverter consumptionConverter;
    
    private final MeterService meterService;
    
    @Inject
    public CollectorService(IConsumptionStorage consumptionStorage, ConsumptionConverter consumptionConverter, MeterService meterService) {
        this.consumptionStorage = consumptionStorage;
        this.consumptionConverter = consumptionConverter;
        this.meterService = meterService;
    }
    
    public void add(String aMeterId, TransactionInputDTO aTransaction) {
        meterService.findOrThrow(aMeterId);
        consumptionConverter.toChunks(aTransaction)
                            .stream()
                            .peek(v -> logger.trace(toJson(v).toString()))
                            .forEach(consumptionStorage::push);
    }
    
}
