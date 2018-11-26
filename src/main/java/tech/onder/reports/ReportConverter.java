package tech.onder.reports;

import tech.onder.consumer.models.PeriodReport;
import tech.onder.reports.models.ReportDTO;

import javax.inject.Inject;

import static tech.onder.meters.OutputUtils.kwhFormat;
import static tech.onder.meters.OutputUtils.tokenPrice;

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

}
