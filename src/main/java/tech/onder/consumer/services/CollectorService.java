package tech.onder.consumer.services;

import com.google.inject.Inject;
import tech.onder.consumer.ChunkConverter;
import tech.onder.consumer.models.ConsumptionChunkReport;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class CollectorService {

    private final ChunkReportManagementService reportRepo;

    @Inject
    public CollectorService(ChunkReportManagementService reportRepo) {
        this.reportRepo = reportRepo;
    }

    public ConsumptionChunkReport getAggregatedValues(String meterUuid) {
        List<ConsumptionChunkReport> r = reportRepo.getForUUID(meterUuid);
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
