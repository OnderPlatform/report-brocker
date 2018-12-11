package tech.onder.consumer;

public class Conf {

    /**Limit for meters queue. 17280 items are enough for meter. But for supplier we need much more. Factually 17280*numbers of consumers*/
    private final Integer itemsLimit = 172800;

    private final Integer numberOfSegments = 144;

    private final Integer reportSegmentLength = 600;

    private final Integer pushInterval = 5;


    public Integer getItemsLimit() {
        return itemsLimit;
    }

    public Integer getNumberOfSegments() {
        return numberOfSegments;
    }

    public Integer getReportSegmentLength() {
        return reportSegmentLength;
    }

    public Integer getPushInterval() {
        return pushInterval;
    }
}
