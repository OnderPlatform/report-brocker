package tech.onder.meters;

import com.sun.istack.internal.NotNull;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.meters.models.BuyerSeller;
import tech.onder.meters.models.Meter;
import tech.onder.meters.models.dto.MeterDTO;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.reports.models.MeterReportDTO;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tech.onder.meters.OutputUtils.*;

public class MeterConverter {

    public Meter fromInputToMeter(MeterInputDTO inputDTO) {
        Meter to = new Meter();
        to.setUuid(inputDTO.getId());
        to.setName(inputDTO.getName());
        to.setComment(inputDTO.getComment());
        return to;
    }

    public BuyerSeller fromInputToRelation(MeterInputDTO meterInputDTO) {
        return new BuyerSeller(meterInputDTO.getId(), meterInputDTO.getParent());
    }

    public MeterDTO toMeterDTO(Meter meter) {
        MeterDTO meterDTO = new MeterDTO();
        meterDTO.setId(meter.getUuid());
        meterDTO.setName(meter.getName());
        meterDTO.setComment(meter.getComment());
        return meterDTO;
    }



    public RelationDTO toMeterRelation(BuyerSeller aRelation) {
        RelationDTO relationDTO = new RelationDTO();
        relationDTO.setMeterId(aRelation.getTo());
        relationDTO.setMeterId(aRelation.getFrom());
        return relationDTO;
    }

    @NotNull
    public <F, T> List<T> transformList(Collection<F> aFrom, Function<F, T> aConverter) {
        return aFrom.stream()
                .map(aConverter)
                .collect(Collectors.toList());
    }



}
