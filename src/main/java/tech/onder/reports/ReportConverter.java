package tech.onder.reports;

import tech.onder.consumer.models.ConsumptionReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.meters.models.dto.MeterDTO;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.ReportDTO;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.List;

import static tech.onder.meters.OutputUtils.*;

public class ReportConverter {
    @Inject
    public ReportConverter() {
    }

    ReportDTO toDTO(PeriodReport chunkReport) {

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTime(chunkReport.getTime());
        reportDTO.setValue(kwhFormat(chunkReport.getConsumption()));
        return reportDTO;
    }

    ReportDTO toPriceDTO(PeriodReport chunkReport) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTime(chunkReport.getTime());
        reportDTO.setValue(tokenPrice(chunkReport.getPrice()));
        return reportDTO;
    }

    public MeterReportDTO toMeterReportDTO(ConsumptionReport aFrom) {
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


    public WebsocketDTO toWebsocketDTO(PeriodReport pr, List<MeterReportDTO> meters) {
        WebsocketDTO websocketDTO = new WebsocketDTO();
        websocketDTO.setInstantPrice(tokenPrice(pr.getPrice()));
        websocketDTO.setInstantConsumption(kwhFormat(pr.getConsumption()));
        websocketDTO.setTime(pr.getTime());
        websocketDTO.setMeters(meters);
        return websocketDTO;
    }
}
