package tech.onder.meters.repositories;

import tech.onder.meters.models.IEntity;

import java.util.Collection;
import java.util.Optional;

public abstract class AbstractInMemoryRepo<I, T extends IEntity> implements IInMemoryRepo<I, T> {
    
    
    @Override
    public Collection<T> all() {
        return this.getValues().values();
    }
    
    @Override
    public Optional<T> find(I id) {
        return Optional.of(this.getValues().get(id));
    }
    
    
}
