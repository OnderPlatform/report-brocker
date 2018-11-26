package tech.onder.meters;

import tech.onder.meters.models.BuyerSeller;
import tech.onder.meters.models.Meter;
import tech.onder.meters.models.dto.MeterInputDTO;

public class MeterConverter {
    public Meter createMeter(MeterInputDTO inputDTO) {
        Meter to = new Meter();
        to.setUuid(inputDTO.getId());
        to.setName(inputDTO.getName());
        to.setComment(inputDTO.getComment());
        return to;
    }

    public BuyerSeller createRelation(MeterInputDTO meterInputDTO) {
        return new BuyerSeller(meterInputDTO.getId(), meterInputDTO.getParent());


    }

}
