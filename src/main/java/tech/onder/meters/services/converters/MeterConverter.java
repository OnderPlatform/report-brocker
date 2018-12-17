package tech.onder.meters.services.converters;

import tech.onder.meters.models.Meter;
import tech.onder.meters.models.dto.MeterDTO;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.models.dto.RelationDTO;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MeterConverter {
    
    public Meter fromInputToMeter(MeterInputDTO inputDTO) {
        Meter to = new Meter();
        to.setUuid(inputDTO.getId());
        to.setName(inputDTO.getName());
        to.setComment(inputDTO.getComment());
        to.setSupplierId(inputDTO.getParentId());
        return to;
    }
    
    
    public MeterDTO toMeterDTO(Meter meter) {
        MeterDTO meterDTO = new MeterDTO();
        meterDTO.setId(meter.getUuid());
        meterDTO.setName(meter.getName());
        meterDTO.setComment(meter.getComment());
        return meterDTO;
    }
    
    
    public RelationDTO toMeterRelation(Meter aMeter) {
        RelationDTO relationDTO = new RelationDTO();
        relationDTO.setMeterUuid(aMeter.getUuid());
        relationDTO.setSellerUuid(aMeter.getSupplierId());
        return relationDTO;
    }
    
    public <F, T> List<T> transformList(Collection<F> aFrom, Function<F, T> aConverter) {
        return aFrom.stream()
                    .map(aConverter)
                    .collect(Collectors.toList());
    }
    
    
}
