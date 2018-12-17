package tech.onder.meters.services;

import tech.onder.common.exceptions.NotFoundException;
import tech.onder.meters.models.Meter;
import tech.onder.meters.models.dto.MeterDTO;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.models.dto.RelationDTO;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.meters.services.converters.MeterConverter;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MeterService {
    
    private final MeterConverter meterConverter;
    
    private final MeterRepo meterRepo;
    
    @Inject
    public MeterService(MeterConverter meterConverter, MeterRepo meterRepo) {
        this.meterConverter = meterConverter;
        this.meterRepo = meterRepo;
    }
    
    public void add(MeterInputDTO meterInputDTO) {
        Meter meter = meterConverter.fromInputToMeter(meterInputDTO);
        meterRepo.save(meter);
    }
    
    public void findOrThrow(String aMeterId) {
        meterRepo.find(aMeterId)
                 .orElseThrow(() -> new NotFoundException(String.format("Meter id: %s has not been found", aMeterId)));
    }
    
    public List<MeterDTO> all() {
        return meterRepo.all().stream().map(meterConverter::toMeterDTO).collect(Collectors.toList());
    }
    
    
    public Collection<RelationDTO> allRelations() {
        return meterRepo.all().stream()
                        .filter((m) -> Objects.nonNull(m.getSupplierId()))
                        .map(meterConverter::toMeterRelation)
                        .collect(Collectors.toList());
    }
    
    public void addList(List<MeterInputDTO> load) {
        load.forEach(this::add);
    }
    
}
