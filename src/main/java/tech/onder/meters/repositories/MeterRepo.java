package tech.onder.meters.repositories;


import tech.onder.meters.models.Meter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MeterRepo extends AbstractInMemoryRepo<String, Meter> {
    
    private final Map<String, Meter> meters;
    
    @Inject
    public MeterRepo() {
        this.meters = new HashMap<>();
    }
    
    @Override
    public Map<String, Meter> getValues() {
        return this.meters;
    }
    
    @Override
    public synchronized void save(Meter model) {
        this.meters.put(model.getUuid(), model);
    }
    
}
