package tech.onder.reports.models;

import java.util.List;

public class WebsocketDTO {
    Long time;
    Double instantConsumption;
    Double instantPrice;
    List<MeterReportDTO> meters;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getInstantConsumption() {
        return instantConsumption;
    }

    public void setInstantConsumption(Double instantConsumption) {
        this.instantConsumption = instantConsumption;
    }

    public Double getInstantPrice() {
        return instantPrice;
    }

    public void setInstantPrice(Double instantPrice) {
        this.instantPrice = instantPrice;
    }

    public List<MeterReportDTO> getMeters() {
        return meters;
    }

    public void setMeters(List<MeterReportDTO> meters) {
        this.meters = meters;
    }
}
