package tech.onder.queue.models.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ConsumptionReportDTO {

    private String meterId;

    private Long updateTime;

    private Long fromTimeMark;

    private Double saleWh;

    private BigInteger saleCost;

    private Double purchaseWh;

    private BigInteger purchaseCost;

    private BigInteger averagePrice;

    public ConsumptionReportDTO() {
    }

    public ConsumptionReportDTO(String meterId) {
        this.meterId = meterId;
    }

    public ConsumptionReportDTO(String meterId, Long fromTimeMark) {
        this.meterId = meterId;
        this.fromTimeMark = fromTimeMark;
    }

    public Long getFromTimeMark() {
        return fromTimeMark;
    }

    public void setFromTimeMark(Long fromTimeMark) {
        this.fromTimeMark = fromTimeMark;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Double getSaleWh() {
        return saleWh;
    }

    public void setSaleWh(Double saleWh) {
        this.saleWh = saleWh;
    }

    public BigInteger getSaleCost() {
        return saleCost;
    }

    public void setSaleCost(BigInteger saleCost) {
        this.saleCost = saleCost;
    }

    public Double getPurchaseWh() {
        return purchaseWh;
    }

    public void setPurchaseWh(Double purchaseWh) {
        this.purchaseWh = purchaseWh;
    }

    public BigInteger getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigInteger purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public BigInteger getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigInteger averagePrice) {
        this.averagePrice = averagePrice;
    }

    @Override
    public String toString() {
        return "ConsumptionChunk{" +
                "id='" + meterId + '\'' +
                ", time=" + LocalDateTime.ofEpochSecond(updateTime, 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                + '}';
    }
}
