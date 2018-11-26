package tech.onder.meters;

import tech.onder.meters.models.BuyerSeller;
import tech.onder.meters.models.Meter;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.repositories.MeterRelationRepo;
import tech.onder.meters.repositories.MeterRepo;

import javax.inject.Inject;

public class MeterService {

    private final MeterConverter meterConverter;

    private final MeterRepo meterRepo;
    private final MeterRelationRepo meterRelationRepo;

    @Inject
    public MeterService(MeterConverter meterConverter, MeterRepo meterRepo, MeterRelationRepo meterRelationRepo) {
        this.meterConverter = meterConverter;
        this.meterRepo = meterRepo;
        this.meterRelationRepo = meterRelationRepo;
    }

    public void add(MeterInputDTO meterInputDTO) {
        Meter meter = meterConverter.createMeter(meterInputDTO);
        meterRepo.save(meter);
        BuyerSeller bs = meterConverter.createRelation(meterInputDTO);
        meterRelationRepo.save(bs);
    }
}
