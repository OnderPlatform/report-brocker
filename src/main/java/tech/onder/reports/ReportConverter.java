package tech.onder.reports;

import tech.onder.consumer.models.PeriodReport;
import tech.onder.reports.models.ReportDTO;

import javax.inject.Inject;

public class ReportConverter {
    @Inject
    public ReportConverter() {
    }

    ReportDTO toDTO(PeriodReport chunkReport) {

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTime(chunkReport.getTime());
        reportDTO.setValue(chunkReport.getConsumption());
        return reportDTO;
    }

    ReportDTO toPriceDTO(PeriodReport chunkReport) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTime(chunkReport.getTime());
        reportDTO.setValue(chunkReport.getPrice());
        return reportDTO;
    }

}
