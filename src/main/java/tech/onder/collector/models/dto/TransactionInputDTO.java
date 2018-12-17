package tech.onder.collector.models.dto;

public class TransactionInputDTO {

    private String sellerId;

    private String buyerId;

    private Long time;

    private Double saleWh;

    private String cost;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getSaleWh() {
        return saleWh;
    }

    public void setSaleWh(Double saleWh) {
        this.saleWh = saleWh;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
