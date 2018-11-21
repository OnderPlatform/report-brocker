package tech.onder.reports;

import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.core.repositories.MeterRepo;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.ReportDTO;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportService {

    /**
     * 10 min /5 sec = 120 ;
     */
    private final ChunkReportManagementService chunkReportManagementService;

    private final MeterRepo meterRepo;

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
                .map(reportConverter::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getPriceReport() {
        return chunkReportManagementService
                .partChunks()
                .values()
                .stream()
                .map(reportConverter::toPriceDTO)
                .collect(Collectors.toList());
    }

    public List<MeterReportDTO> getMeters() {
        return chunkReportManagementService.meterReportDTOS();
    }


}
