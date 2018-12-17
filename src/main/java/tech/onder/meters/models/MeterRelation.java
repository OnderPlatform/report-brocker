package tech.onder.meters.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeterRelation implements IEntity<String> {
    
    @JsonProperty("sellerUuid")
    private String from;
    
    @JsonProperty("meterUuid")
    private String to;
    
    public MeterRelation(String to, String from) {
        this.from = from;
        this.to = to;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    @Override
    public String getId() {
        return to;
    }
    
}
