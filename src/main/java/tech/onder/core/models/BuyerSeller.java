package tech.onder.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BuyerSeller {

    @JsonProperty("meterUuid")
    private String from;
    @JsonProperty("sellerUuid")
    private String to;

    public BuyerSeller(String from, String to) {
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
}
