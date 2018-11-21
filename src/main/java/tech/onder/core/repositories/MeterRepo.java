package tech.onder.core.repositories;


import tech.onder.core.models.Meter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MeterRepo extends AbstractInMemoryRepo<String, Meter> {

    private final Map<String, Meter> meters;

    @Inject
    public MeterRepo() {
        Map<String, Meter> map = new HashMap<>();
        map.put("0x230", Meter.generate("0x230"));
        map.put("0x234", Meter.generate("0x234"));
        map.put("0x235", Meter.generate("0x235"));
        map.put("0x236", Meter.generate("0x236"));

        map.put("0x240", Meter.generate("0x240"));
        map.put("0x241", Meter.generate("0x241"));
        map.put("0x242", Meter.generate("0x242"));

        this.meters = map;
    }

    @Override
    public Map<String, Meter> getValues() {
        return this.meters;
    }
}
