package tech.onder.collector.services.converters;

import tech.onder.queue.models.ConsumptionChunk;
import tech.onder.collector.models.dto.TransactionInputDTO;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ConsumptionConverter {
    
    public List<ConsumptionChunk> toChunks(TransactionInputDTO inputDTO) {
        return Arrays.asList(createPurchase(inputDTO), createSale(inputDTO));
    }
    
    private ConsumptionChunk createSale(TransactionInputDTO dto) {
        ConsumptionChunk chunk = new ConsumptionChunk();
        chunk.setUuid(dto.getSellerId());
        chunk.setSaleWh(dto.getSaleWh());
        chunk.setSaleCost(new BigInteger(dto.getCost()));
        chunk.setPurchaseCost(BigInteger.ZERO);
        chunk.setPurchaseWh(0.0);
        chunk.setTime(dto.getTime());
        return chunk;
    }
    
    private ConsumptionChunk createPurchase(TransactionInputDTO dto) {
        ConsumptionChunk chunk = new ConsumptionChunk();
        chunk.setUuid(dto.getBuyerId());
        chunk.setPurchaseWh(dto.getSaleWh());
        chunk.setPurchaseCost(new BigInteger(dto.getCost()));
        chunk.setSaleCost(BigInteger.ZERO);
        chunk.setSaleWh(0.0);
        chunk.setTime(dto.getTime());
        return chunk;
    }
    
}
