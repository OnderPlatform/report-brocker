package tech.onder.queue.models.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class PeriodReportDTO {
    
    private Long time;
    
    private Double consumption;
    
    private BigInteger price;
    
    public Long getTime() {
        return time;
    }
    
    public void setTime(Long time) {
        this.time = time;
    }
    
    public Double getConsumption() {
        return consumption;
    }
    
    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }
    
    public BigInteger getPrice() {
        return price;
    }
    
    public void setPrice(BigInteger price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "PeriodReportDTO{" +
                       "time=" + LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
                                              .format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                       ", consumption=" + consumption +
                       ", price=" + price +
                       '}';
    }
    
}
