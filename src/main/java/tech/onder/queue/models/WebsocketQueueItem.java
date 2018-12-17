package tech.onder.queue.models;

import tech.onder.queue.models.dto.PeriodReportDTO;
import tech.onder.queue.models.dto.ConsumptionReportDTO;

import java.util.Comparator;
import java.util.Map;

public class WebsocketQueueItem implements Comparator<WebsocketQueueItem> {
    
    private Long segmentMark;
    
    private PeriodReportDTO periodReportDTO;
    
    private Map<String, ConsumptionReportDTO> metersConsumption;
    
    @Override
    public int compare(WebsocketQueueItem item, WebsocketQueueItem item2) {
        return (int) (item.segmentMark - item2.segmentMark);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WebsocketQueueItem) {
            return this.segmentMark.equals(((WebsocketQueueItem) obj).segmentMark);
        }
        return false;
    }
    
    public WebsocketQueueItem(PeriodReportDTO periodReportDTO, Map<String, ConsumptionReportDTO> metersConsumption) {
        this.segmentMark = periodReportDTO.getTime();
        this.periodReportDTO = periodReportDTO;
        this.metersConsumption = metersConsumption;
    }
    
    public Long getSegmentMark() {
        return segmentMark;
    }
    
    public void setSegmentMark(Long segmentMark) {
        this.segmentMark = segmentMark;
    }
    
    public PeriodReportDTO getPeriodReportDTO() {
        return periodReportDTO;
    }
    
    public void setPeriodReportDTO(PeriodReportDTO periodReportDTO) {
        this.periodReportDTO = periodReportDTO;
    }
    
    
    public Map<String, ConsumptionReportDTO> getMetersConsumption() {
        return metersConsumption;
    }
    
    public void setMetersConsumption(Map<String, ConsumptionReportDTO> metersConsumption) {
        this.metersConsumption = metersConsumption;
    }
    
}
