package tech.onder.consumer;

import tech.onder.consumer.models.*;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.WebsocketDTO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static tech.onder.meters.OutputUtils.*;

public class ChunkConverter {

    private final MeterRepo meterRepo;

    public static BigInteger calculatePrice(TransactionInputDTO inputDTO) {
        return calculatePrice(new BigInteger(inputDTO.getCost()), inputDTO.getSaleWh());
    }

    public static BigInteger calculatePrice(BigInteger cost, Double kw) {
        if (new BigDecimal(kw).equals(BigDecimal.ZERO)) {
            return BigInteger.ZERO;
        }
        return new BigDecimal(cost).divide(new BigDecimal(kw), 2, RoundingMode.HALF_UP).toBigInteger();
    }

    public static BigInteger calculatePrice(ConsumptionReport consumptionReport) {
        BigInteger castUp;
        Double castWh;
        if (!consumptionReport.getPurchaseCost().equals(BigInteger.ZERO)) {
            castUp = consumptionReport.getPurchaseCost();
            castWh = consumptionReport.getPurchaseWh();
        } else {
            castUp = consumptionReport.getSaleCost();
            castWh = consumptionReport.getSaleWh();
        }
        return calculatePrice(castUp, castWh);
    }




    public MeterReportDTO toMeterDTO(ConsumptionChunkReport chunkReport) {
        MeterReportDTO mrd = new MeterReportDTO();
        mrd.setId(chunkReport.getUuid());
        mrd.setUpdateTime(chunkReport.getTime());
        mrd.setPurchaseKwh(kwhFormat(chunkReport.getPurchaseWh()));
        mrd.setPurchaseTokens(token(chunkReport.getPurchaseCost()));
        mrd.setSaleKwh(kwhFormat(chunkReport.getSaleWh()));
        mrd.setSaleTokens(token(chunkReport.getSaleCost()));
        mrd.setPrice(tokenPrice(ChunkConverter.calculatePrice(chunkReport)));
        return mrd;
    }



    public List<MeterReportDTO> toMeterReportsList(List<ConsumptionChunkReport> aChunks) {
        meterRepo.all()
                .stream()
                .map(m ->
                        Optional.ofNullable(websocketItem.getMetersConsumption().get(m.getUuid()))
                                .map(chunkConverter::toMeterDTO)
                                .orElseGet(() -> chunkConverter.toMeterDTO(m)))
                .collect(Collectors.toList());
    }

    public WebsocketDTO websocketOutput(WebsocketQueueItem websocketItem) {
        List<MeterReportDTO> meterReportDTOS =
        return chunkConverter.toWebsocketDTO(websocketItem.getPeriodReport(), meterReportDTOS);
    }


}
