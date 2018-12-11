package tech.onder.consumer.models;

import java.util.Map;

public class WebsocketQueueItem {

    private Long markDate;

    private PeriodReport periodReport;

    private Map<String, ConsumptionReport> metersConsumption;

    public WebsocketQueueItem(PeriodReport periodReport, Map<String, ConsumptionReport> metersConsumption) {
        this.markDate = periodReport.getTime();
        this.periodReport = periodReport;
        this.metersConsumption = metersConsumption;
    }

    public PeriodReport getPeriodReport() {
        return periodReport;
    }

    public void setPeriodReport(PeriodReport periodReport) {
        this.periodReport = periodReport;
    }
    

    public Map<String, ConsumptionReport> getMetersConsumption() {
        return metersConsumption;
    }

    public void setMetersConsumption(Map<String, ConsumptionReport> metersConsumption) {
        this.metersConsumption = metersConsumption;
    }
}
