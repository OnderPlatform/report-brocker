package tech.onder.reports;

import tech.onder.consumer.repository.ChunkReportRepo;
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
    private final ChunkReportRepo chunkReportRepo;

    private final MeterRepo meterRepo;

    private final ReportConverter reportConverter;

    @Inject
    public ReportService(ChunkReportRepo chunkReportRepo, MeterRepo meterRepo, ReportConverter reportConverter) {
        this.chunkReportRepo = chunkReportRepo;
        this.meterRepo = meterRepo;
        this.reportConverter = reportConverter;
    }

    public List<ReportDTO> getComsumptionReport() {

        return chunkReportRepo
                .partChunks()
                .values()
                .stream()
                .map(reportConverter::toDTO).collect(Collectors.toList());
    }

    public List<ReportDTO> getPriceReport() {
        return chunkReportRepo
                .partChunks()
                .values()
                .stream()
                .map(reportConverter::toPriceDTO)
                .collect(Collectors.toList());
    }

    public List<MeterReportDTO> websocketUpdate(){

Set<String> meterUUIDs = meterRepo.getValues().keySet();
        chunkReportRepo.get()
    }
}
