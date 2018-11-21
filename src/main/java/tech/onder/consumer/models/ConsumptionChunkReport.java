package tech.onder.consumer.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumptionChunkReport {

    private String uuid;

    private Long time;

    private Double price;

    private Double saleKwh;

    private Double saleCost;

    private Double purchaseKwh;

    private Double purchaseCost;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSaleKwh() {
        return saleKwh;
    }

    public void setSaleKwh(Double soleKwh) {
        this.saleKwh = soleKwh;
    }

    public Double getSaleCost() {
        return saleCost;
    }

    public void setSaleCost(Double saleCost) {
        this.saleCost = saleCost;
    }

    public Double getPurchaseKwh() {
        return purchaseKwh;
    }

    public void setPurchaseKwh(Double purchaseKwh) {
        this.purchaseKwh = purchaseKwh;
    }

    public Double getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(Double purchaseCost) {
        this.purchaseCost = purchaseCost;
    }
}
