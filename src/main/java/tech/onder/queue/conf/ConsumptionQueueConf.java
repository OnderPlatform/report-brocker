package tech.onder.queue.conf;

import com.typesafe.config.Config;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ConsumptionQueueConf {
    
    /**
     * Limit for meters queue. 17280 items are enough for meter. But for supplier we need much more.
     * Factually 17280*numbers of consumers
     */
    private final Integer itemsLimit = 172800;
    
    private final Integer numberOfSegments = 144;
    
    private final Integer reportSegmentLength = 600;
    
    private final Integer pushInterval = 5;
    
    private final String backupfile = "conf/meters-storage-back.json";
    
    private Config configuration;
    
    @Inject
    public ConsumptionQueueConf(Config configuration) {
        this.configuration = configuration;
    }
    
    public Integer getItemsLimit() {
        
        return Optional.ofNullable(configuration.getInt("queue.itemsLimit"))
                       .orElse(this.itemsLimit);
    }
    
    public Integer getNumberOfSegments() {
        return Optional.ofNullable(configuration.getInt("queue.report.segments.numbers"))
                       .orElse(this.numberOfSegments);
    }
    
    public Integer getReportSegmentLength() {
        return Optional.ofNullable(configuration.getInt("queue.report.segments.length"))
                       .orElse(this.reportSegmentLength);
        
        
    }
    
    public Integer getPushInterval() {
        return Optional.ofNullable(configuration.getInt("queue.pushInterval"))
                       .orElse(this.pushInterval);
        
    }
    
    public String getBackupFilename() {
        return Optional.ofNullable(configuration.getString("queue.backupFile"))
                       .orElse(this.backupfile);
    }
    
}
