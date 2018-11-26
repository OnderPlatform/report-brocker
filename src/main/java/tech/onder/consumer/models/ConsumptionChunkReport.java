package tech.onder.consumer.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumptionChunkReport {

    private String uuid;

    private Long time;

    private BigInteger price;

    private Double saleWh = 0.0;

    private BigInteger saleCost;

    private Double purchaseWh = 0.0;

    private BigInteger purchaseCost;

    public ConsumptionChunkReport() {
    }

    public ConsumptionChunkReport(String uuid) {
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

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
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
}
