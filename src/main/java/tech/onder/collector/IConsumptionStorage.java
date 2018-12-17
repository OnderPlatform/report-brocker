package tech.onder.collector;

import tech.onder.queue.models.ConsumptionChunk;

public interface IConsumptionStorage {
    
    void push(ConsumptionChunk chunk);
}
