package tech.onder.queue;

import java.util.List;

public interface IRestorableStorage<T> {
    
    void loadAll(List<T> chunkReports);
    
    List<T> dump();
    
}
