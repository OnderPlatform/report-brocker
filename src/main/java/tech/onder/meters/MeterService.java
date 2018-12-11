package tech.onder.meters;

import tech.onder.consumer.exceptions.NotFoundException;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.meters.models.BuyerSeller;
import tech.onder.meters.models.Meter;
import tech.onder.meters.models.dto.MeterDTO;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.repositories.MeterRelationRepo;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.reports.models.MeterReportDTO;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MeterService {


    private final MeterConverter meterConverter;

    private final MeterRepo meterRepo;

    private final MeterRelationRepo meterRelationRepo;

    private final MeterRelationRepo meterRelationRepo;

    private final ChunkReportManagementService chunkReportManagementService;

    @Inject
    public MeterService(MeterConverter meterConverter, MeterRepo meterRepo, MeterRelationRepo meterRelationRepo) {
        this.meterConverter = meterConverter;
        this.meterRepo = meterRepo;
        this.meterRelationRepo = meterRelationRepo;
    }

    public void add(MeterInputDTO meterInputDTO) {
        Meter meter = meterConverter.fromInputToMeter(meterInputDTO);
        meterRepo.save(meter);
        BuyerSeller bs = meterConverter.fromInputToRelation(meterInputDTO);
        meterRelationRepo.save(bs);
    }

    public void findOrThrow(String uuid) {
        meterRepo.find(uuid).orElseThrow(() -> new NotFoundException(String.format("Meter id: %s has not been found", uuid)));
    }

    public List<MeterDTO> all() {
        return meterRepo.all().stream().map(meterConverter::toMeterDTO).collect(Collectors.toList());
    }



    public Collection<RelationDTO> allRelations() {
        return meterConverter.transformList(meterRelationRepo.all(), meterConverter::toMeterRelation);
    }
}
