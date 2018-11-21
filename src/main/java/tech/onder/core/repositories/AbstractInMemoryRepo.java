package tech.onder.core.repositories;

import java.util.Collection;
import java.util.Optional;

public abstract class AbstractInMemoryRepo<I, T> implements IInMemoryRepo<I, T> {


    @Override
    public Collection<T> all() {
        return this.getValues().values();
    }

    @Override
    public Optional<T> find(I id) {
        return Optional.of(this.getValues().get(id));
    }


}
