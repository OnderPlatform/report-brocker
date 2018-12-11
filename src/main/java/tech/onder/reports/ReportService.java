package tech.onder.reports;

import tech.onder.consumer.models.ConsumptionReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.consumer.models.WebsocketQueueItem;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.meters.MeterService;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.ReportDTO;
import tech.onder.reports.models.WebsocketDTO;

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
    private final ChunkReportManagementService chunkReportManagementService;

    private final MeterRepo meterRepo;

    private final MeterService meterService;

    private final ReportConverter reportConverter;

    @Inject
    public ReportService(ChunkReportManagementService chunkReportManagementService, MeterRepo meterRepo, ReportConverter reportConverter) {
        this.chunkReportManagementService = chunkReportManagementService;
        this.meterRepo = meterRepo;
        this.reportConverter = reportConverter;
    }

    public List<ReportDTO> getComsumptionReport() {

        return chunkReportManagementService
                .partChunks()
                .values()
                .stream()
                .sorted(Comparator.comparing(PeriodReport::getTime))
                .map(reportConverter::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getPriceReport() {
        return chunkReportManagementService
                .partChunks()
                .values()
                .stream()
                .sorted(Comparator.comparing(PeriodReport::getTime))
                .map(reportConverter::toPriceDTO)
                .collect(Collectors.toList());
    }


    public List<MeterReportDTO> getMeters() {
        return toMeterReports(chunkReportManagementService::meterReports);
    }

    private List<MeterReportDTO> toMeterReports(Supplier<Map<String, ConsumptionReport>> consumptionReportMapSupplier) {
        Map<String, ConsumptionReport> consumptionReportMap = consumptionReportMapSupplier.get();
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

    public WebsocketDTO websocketOutput(WebsocketQueueItem websocketItem) {
        List<MeterReportDTO> meters = this.toMeterReports(websocketItem::getMetersConsumption);
        return reportConverter.toWebsocketDTO(websocketItem.getPeriodReport(), meters);
    }


}
