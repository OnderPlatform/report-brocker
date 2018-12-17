package tech.onder.reports.models.dto;

import java.util.List;

public class WebsocketDTO {
    Long time;
    String instantConsumption;
    String instantPrice;
    List<MeterReportDTO> meters;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getInstantConsumption() {
        return instantConsumption;
    }

    public void setInstantConsumption(String instantConsumption) {
        this.instantConsumption = instantConsumption;
    }

    public String getInstantPrice() {
        return instantPrice;
    }

    public void setInstantPrice(String instantPrice) {
        this.instantPrice = instantPrice;
    }

    public List<MeterReportDTO> getMeters() {
        return meters;
    }

    public void setMeters(List<MeterReportDTO> meters) {
        this.meters = meters;
    }
}
