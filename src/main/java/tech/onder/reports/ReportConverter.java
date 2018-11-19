package tech.onder.reports;

import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.reports.models.ReportDTO;

public class ReportConverter {

   ReportDTO toDTO(ConsumptionChunkReport chunkReport){

       ReportDTO reportDTO = new ReportDTO();
       reportDTO.setTime(chunkReport.getTime());
       reportDTO.setValue(chunkReport.getPurchaseKwh());
       return reportDTO;
   }

   ReportDTO toPriceDTO(ConsumptionChunkReport chunkReport){
       ReportDTO reportDTO = new ReportDTO();
       reportDTO.setTime(chunkReport.getTime());
       reportDTO.setValue(chunkReport.getPurchaseCost()/chunkReport.getPurchaseKwh());
       return reportDTO;
   }

}
