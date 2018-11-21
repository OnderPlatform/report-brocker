package tech.onder.core.repositories;

import tech.onder.core.models.BuyerSeller;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
@Singleton
public class MeterRelationRepo extends AbstractInMemoryRepo<String, BuyerSeller> {

    private final Map<String, BuyerSeller> values;
@Inject
    public MeterRelationRepo() {
        Map<String, BuyerSeller> relMap = new HashMap<>();
        relMap.put("0x234", new BuyerSeller("0x234", "0x230"));
        relMap.put("0x235", new BuyerSeller("0x235", "0x230"));
        relMap.put("0x236", new BuyerSeller("0x236", "0x230"));
        relMap.put("0x241", new BuyerSeller("0x241", "0x240"));
        relMap.put("0x242", new BuyerSeller("0x242", "0x240"));


        this.values = relMap;
    }

    @Override
    public Map<String, BuyerSeller> getValues() {
        return values;
    }
}
