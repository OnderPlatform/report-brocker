package tech.onder.consumer.converters;

import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.TransactionInputDTO;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class ConsumptionConverter {

    public List<ConsumptionChunkReport> toChunks(TransactionInputDTO inputDTO) {
        return Arrays.asList(createPurchase(inputDTO), createSale(inputDTO));
    }

    private ConsumptionChunkReport createSale(TransactionInputDTO dto) {

        ConsumptionChunkReport chunk = new ConsumptionChunkReport();
        chunk.setUuid(dto.getSellerId());
        chunk.setSaleWh(dto.getSaleWh());
        chunk.setSaleCost(new BigInteger(dto.getCost()));
        chunk.setPurchaseCost(BigInteger.ZERO);
        chunk.setPurchaseWh(0.0);
        chunk.setTime(dto.getTime());
        return chunk;
    }

    private ConsumptionChunkReport createPurchase(TransactionInputDTO dto) {

        ConsumptionChunkReport chunk = new ConsumptionChunkReport();
        chunk.setUuid(dto.getBuyerId());
        chunk.setPurchaseWh(dto.getSaleWh());
        chunk.setPurchaseCost(new BigInteger(dto.getCost()));
        chunk.setSaleCost(BigInteger.ZERO);
        chunk.setSaleWh(0.0);
        chunk.setTime(dto.getTime());
        return chunk;
    }
}
