package tech.onder.reports.services;

import tech.onder.queue.models.dto.ConsumptionReportDTO;
import tech.onder.queue.models.dto.PeriodReportDTO;
import tech.onder.queue.models.WebsocketQueueItem;
import tech.onder.reports.IReportDataSupplier;
import tech.onder.meters.services.MeterService;
import tech.onder.reports.services.converters.ReportConverter;
import tech.onder.reports.models.dto.MeterReportDTO;
import tech.onder.reports.models.dto.ReportDTO;
import tech.onder.reports.models.dto.WebsocketDTO;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ReportService {
    
    /**
     * 10 min /5 sec = 120 ;
     */
    private final IReportDataSupplier reportDataSupplier;
    
    private final MeterService meterService;
    
    private final ReportConverter reportConverter;
    
    @Inject
    public ReportService(IReportDataSupplier reportDataSupplier, MeterService meterService, ReportConverter reportConverter) {
        this.reportDataSupplier = reportDataSupplier;
        this.meterService = meterService;
        this.reportConverter = reportConverter;
    }
    
    public List<ReportDTO> getConsumptionReport() {
        
        return reportDataSupplier
                       .periodReportBySegments()
                       .values()
                       .stream()
                       .sorted(Comparator.comparing(PeriodReportDTO::getTime))
                       .map(reportConverter::toDTO)
                       .collect(Collectors.toList());
    }
    
    public List<ReportDTO> getPriceReport() {
        return reportDataSupplier.periodReportBySegments().values().stream()
                                 .sorted(Comparator.comparing(PeriodReportDTO::getTime))
                                 .map(reportConverter::toPriceDTO)
                                 .collect(Collectors.toList());
    }
    
    public List<MeterReportDTO> getMeters() {
        return toMeterReports(reportDataSupplier::meterConsumptionReports);
    }
    
    private List<MeterReportDTO> toMeterReports(Supplier<Map<String, ConsumptionReportDTO>> aConsumptionReportMapSupplier) {
        Map<String, ConsumptionReportDTO> consumptionReportMap = aConsumptionReportMapSupplier.get();
        return meterService.all()
                           .stream()
                           // .map(reportConverter::createEmptyMeterReportDTO)
                           .map(m ->
                                        Optional.ofNullable(consumptionReportMap.get(m.getId()))
                                                .map(reportConverter::toMeterReportDTO)
                                                .orElseGet(() -> reportConverter.createEmptyMeterReportDTO(m))
                           )
                           .collect(Collectors.toList());
    }
    
    public WebsocketDTO websocketOutput(WebsocketQueueItem aWebsocketItem) {
        List<MeterReportDTO> meters = this.toMeterReports(aWebsocketItem::getMetersConsumption);
        return reportConverter.toWebsocketDTO(aWebsocketItem.getPeriodReportDTO(), meters);
    }
    
    
}
