package tech.onder.meters.repositories;


import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface IInMemoryRepo<I, T> {

    Map<I, T> getValues();

    Collection<T> all();

    Optional<T> find(I id);



}
