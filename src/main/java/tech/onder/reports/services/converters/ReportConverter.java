package tech.onder.reports.services.converters;

import tech.onder.queue.models.dto.ConsumptionReportDTO;
import tech.onder.queue.models.dto.PeriodReportDTO;
import tech.onder.meters.models.dto.MeterDTO;
import tech.onder.reports.models.dto.MeterReportDTO;
import tech.onder.reports.models.dto.ReportDTO;
import tech.onder.reports.models.dto.WebsocketDTO;

import java.math.BigInteger;
import java.util.List;

import static tech.onder.reports.utils.OutputUtils.*;

public class ReportConverter {
    
    
    public ReportDTO toDTO(PeriodReportDTO chunkReport) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTime(chunkReport.getTime());
        reportDTO.setValue(kwhFormat(chunkReport.getConsumption()));
        return reportDTO;
    }
    
    public ReportDTO toPriceDTO(PeriodReportDTO chunkReport) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTime(chunkReport.getTime());
        reportDTO.setValue(tokenPrice(chunkReport.getPrice()));
        return reportDTO;
    }
    
    public MeterReportDTO toMeterReportDTO(ConsumptionReportDTO aFrom) {
        MeterReportDTO reportDTO = new MeterReportDTO();
        reportDTO.setUpdateTime(aFrom.getUpdateTime());
        reportDTO.setPrice(tokenPrice(aFrom.getAveragePrice()));
        reportDTO.setPurchaseKwh(kwhFormat(aFrom.getPurchaseWh()));
        reportDTO.setPurchaseTokens(token(aFrom.getPurchaseCost()));
        reportDTO.setSaleKwh(kwhFormat(aFrom.getSaleWh()));
        reportDTO.setSaleTokens(token(aFrom.getSaleCost()));
        reportDTO.setPrice(tokenPrice(aFrom.getAveragePrice()));
        reportDTO.setId(aFrom.getMeterId());
        return reportDTO;
    }
    
    public MeterReportDTO createEmptyMeterReportDTO(MeterDTO aFrom) {
        MeterReportDTO mrd = new MeterReportDTO();
        mrd.setId(aFrom.getId());
        mrd.setUpdateTime(0L);
        mrd.setPurchaseKwh(kwhFormat(0.0));
        mrd.setPurchaseTokens(token(BigInteger.ZERO));
        mrd.setSaleKwh(kwhFormat(0.0));
        mrd.setSaleTokens(token(BigInteger.ZERO));
        mrd.setPrice(tokenPrice(BigInteger.ZERO));
        return mrd;
    }
    
    
    public WebsocketDTO toWebsocketDTO(PeriodReportDTO pr, List<MeterReportDTO> meters) {
        WebsocketDTO websocketDTO = new WebsocketDTO();
        websocketDTO.setInstantPrice(tokenPrice(pr.getPrice()));
        websocketDTO.setInstantConsumption(kwhFormat(pr.getConsumption()));
        websocketDTO.setTime(pr.getTime());
        websocketDTO.setMeters(meters);
        return websocketDTO;
    }
    
}
