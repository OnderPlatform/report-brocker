package tech.onder.reports;

import tech.onder.queue.models.dto.ConsumptionReportDTO;
import tech.onder.queue.models.dto.PeriodReportDTO;

import java.util.Map;

public interface IReportDataSupplier {
    
    Map<String, ConsumptionReportDTO> meterConsumptionReports();
    
    Map<Long, PeriodReportDTO> periodReportBySegments();
}
