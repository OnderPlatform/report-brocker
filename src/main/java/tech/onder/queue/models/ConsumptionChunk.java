package tech.onder.queue.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumptionChunk {
    
    private String uuid;
    
    private Long time;
    
    private Double saleWh = 0.0;
    
    private BigInteger saleCost;
    
    private Double purchaseWh = 0.0;
    
    private BigInteger purchaseCost;
    
    public ConsumptionChunk() {
    }
    
    public ConsumptionChunk(String uuid) {
        this.uuid = uuid;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public Long getTime() {
        return time;
    }
    
    public void setTime(Long time) {
        this.time = time;
    }
    
    public Double getPurchaseWh() {
        return purchaseWh;
    }
    
    public void setPurchaseWh(Double purchaseWh) {
        this.purchaseWh = purchaseWh;
    }
    
    public Double getSaleWh() {
        return saleWh;
    }
    
    public void setSaleWh(Double soleKwh) {
        this.saleWh = soleKwh;
    }
    
    public BigInteger getSaleCost() {
        return saleCost;
    }
    
    public void setSaleCost(BigInteger saleCost) {
        this.saleCost = saleCost;
    }
    
    public BigInteger getPurchaseCost() {
        return purchaseCost;
    }
    
    public void setPurchaseCost(BigInteger purchaseCost) {
        this.purchaseCost = purchaseCost;
    }
    
    @Override
    public String toString() {
        return "ConsumptionChunk{" +
                       "uuid='" + uuid + '\'' +
                       ", time=" + LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC)
                                                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                       + '}';
    }
    
}
