package tech.onder.consumer;

import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.MeterInputDTO;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.WebsocketDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tech.onder.meters.OutputUtils.kwhFormat;
import static tech.onder.meters.OutputUtils.token;
import static tech.onder.meters.OutputUtils.tokenPrice;

public class ChunkConverter {
    List<ConsumptionChunkReport> toChunks(MeterInputDTO inputDTO) {
        BigInteger price = calculatePrice(inputDTO);

        return Arrays.asList(createPurchase(inputDTO), createSale(inputDTO)).stream().peek(d -> d.setPrice(price)).collect(Collectors.toList());
    }

    public static BigInteger calculatePrice(MeterInputDTO inputDTO) {
        return calculatePrice(new BigInteger(inputDTO.getCost()),inputDTO.getSaleWh());
    }
    public static BigInteger calculatePrice(BigInteger cost, Double kw){
        if(new BigDecimal(kw).equals(BigDecimal.ZERO)){
            return BigInteger.ZERO;
        }
        return new BigDecimal(cost).divide(new BigDecimal(kw),2, RoundingMode.HALF_UP).toBigInteger();
    }

    public static BigInteger calculatePrice(ConsumptionChunkReport chunkReport) {
        BigInteger castUp;
        Double castWh;
        if (!chunkReport.getPurchaseCost().equals(BigInteger.ZERO)) {
            castUp = chunkReport.getPurchaseCost();
            castWh = chunkReport.getPurchaseWh();
        } else {
            castUp = chunkReport.getSaleCost();
            castWh = chunkReport.getSaleWh();
        }
        return calculatePrice(castUp, castWh);
    }

    public ConsumptionChunkReport createSale(MeterInputDTO dto) {

        ConsumptionChunkReport chunk = new ConsumptionChunkReport();
        chunk.setUuid(dto.getSellerId());
        chunk.setSaleWh(dto.getSaleWh());
        chunk.setSaleCost(new BigInteger(dto.getCost()));
        chunk.setPurchaseCost(BigInteger.ZERO);
        chunk.setPurchaseWh(0.0);
        chunk.setTime(dto.getTime());
        return chunk;
    }

    public ConsumptionChunkReport createPurchase(MeterInputDTO dto) {

        ConsumptionChunkReport chunk = new ConsumptionChunkReport();
        chunk.setUuid(dto.getBuyerId());
        chunk.setPurchaseWh(dto.getSaleWh());
        chunk.setPurchaseCost(new BigInteger(dto.getCost()));
        chunk.setSaleCost(BigInteger.ZERO);
        chunk.setSaleWh(0.0);
        chunk.setTime(dto.getTime());
        return chunk;
    }

    public MeterReportDTO toMeterDTO(ConsumptionChunkReport chunkReport) {
        MeterReportDTO mrd = new MeterReportDTO();
        mrd.setUuid(chunkReport.getUuid());
        mrd.setUpdateTime(chunkReport.getTime());
        mrd.setPurchaseKwh(kwhFormat(chunkReport.getPurchaseWh() ));
        mrd.setPurchaseTokens(token(chunkReport.getPurchaseCost()));
        mrd.setSaleKwh(kwhFormat(chunkReport.getSaleWh()));
        mrd.setSaleTokens(token(chunkReport.getSaleCost()));
        mrd.setPrice(tokenPrice(ChunkConverter.calculatePrice(chunkReport)));
        return mrd;
    }
    public WebsocketDTO toWebsocketDTO(PeriodReport pr, List<MeterReportDTO> meters){
        WebsocketDTO websocketDTO = new WebsocketDTO();
        websocketDTO.setInstantPrice(tokenPrice(pr.getPrice()));
        websocketDTO.setInstantConsumption(kwhFormat(pr.getConsumption()));
        websocketDTO.setTime(pr.getTime());
        websocketDTO.setMeters(meters);
        return websocketDTO;
    }
}
