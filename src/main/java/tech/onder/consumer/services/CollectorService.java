package tech.onder.consumer.services;

import com.google.inject.Inject;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.repository.ChunkReportRepo;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class CollectorService {

    private final ChunkReportRepo reportRepo;

    @Inject
    public CollectorService(ChunkReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    public ConsumptionChunkReport getAggregatedValues(String meterUuid) {
        List<ConsumptionChunkReport> r = reportRepo.get(meterUuid);
        ConsumptionChunkReport agrrReport = r.stream()
                .reduce((a, b) -> {
                    a.setPrice(a.getPrice() + b.getPrice());
                    a.setPurchaseKwh(a.getPurchaseKwh() + b.getPurchaseKwh());
                    a.setPurchaseCost(a.getPurchaseCost() + b.getPurchaseCost());
                    a.setSaleKwh(a.getSaleKwh() + b.getSaleKwh());
                    a.setSaleCost(a.getPurchaseCost() + b.getPurchaseCost());
                    return a;
                })
                .orElse(new ConsumptionChunkReport());

        Long timeMark = r.stream()
                .mapToLong(ConsumptionChunkReport::getTime)
                .max()
                .orElse(LocalDateTime.now().minusHours(24).toEpochSecond(UTC));

        agrrReport.setTime(timeMark);
        return agrrReport;
    }


}
