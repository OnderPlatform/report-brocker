package tech.onder.reports.models;


public class MeterReportDTO {

    private String uuid;

    private Long updateTime;

    private Double price;

    private Double saleKwh;

    private Double saleTokens;

    private Double purchaseKwh;

    private Double purchaseTokens;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
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

    public void setSaleKwh(Double saleKwh) {
        this.saleKwh = saleKwh;
    }

    public Double getSaleTokens() {
        return saleTokens;
    }

    public void setSaleTokens(Double saleTokens) {
        this.saleTokens = saleTokens;
    }

    public Double getPurchaseKwh() {
        return purchaseKwh;
    }

    public void setPurchaseKwh(Double purchaseKwh) {
        this.purchaseKwh = purchaseKwh;
    }

    public Double getPurchaseTokens() {
        return purchaseTokens;
    }

    public void setPurchaseTokens(Double purchaseTokens) {
        this.purchaseTokens = purchaseTokens;
    }
}
