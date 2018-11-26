package tech.onder.reports;

import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.models.PeriodReport;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.ReportDTO;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

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
        return chunkReportManagementService.meterReportDTOS();
    }


    public ConsumptionChunkReport getAggregatedValues(String meterUuid) {
        List<ConsumptionChunkReport> r = chunkReportManagementService.getForUUID(meterUuid);
        ConsumptionChunkReport agrrReport = r.stream()
                .reduce((a, b) -> {
                    a.setPurchaseWh(a.getPurchaseWh() + b.getPurchaseWh());
                    a.setPurchaseCost(a.getPurchaseCost().add(b.getPurchaseCost()));
                    a.setSaleWh(a.getSaleWh() + b.getSaleWh());
                    a.setSaleCost(a.getSaleCost().add(b.getSaleCost()));
                    return a;
                })
                .orElse(new ConsumptionChunkReport());
        agrrReport.setPrice(ChunkConverter.calculatePrice(agrrReport));
        Long timeMark = r.stream()
                .mapToLong(ConsumptionChunkReport::getTime)
                .max()
                .orElse(LocalDateTime.now().minusHours(24).toEpochSecond(UTC));

        agrrReport.setTime(timeMark);
        return agrrReport;
    }


}
