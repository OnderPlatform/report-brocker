package tech.onder.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.services.ChunkReportManagementService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;

public class CollectorController extends Controller {

    private final Logger logger = LoggerFactory.getLogger(CollectorController.class);

    private HttpExecutionContext ec;

    private final ChunkReportManagementService chunkReportManagementService;

    @Inject
    public CollectorController(HttpExecutionContext ec, ChunkReportManagementService chunkReportManagementService) {
        this.ec = ec;
        this.chunkReportManagementService = chunkReportManagementService;
    }

    public CompletionStage<Result> push(String uuid) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        JsonNode amount = ctx().request().body().asJson();

                        ConsumptionChunkReport chunk = Json.fromJson(amount, ConsumptionChunkReport.class);
                        if(chunk.getSaleKwh()==null&&chunk.getPurchaseKwh()==null){
                            return badRequest();
                        }
                        chunkReportManagementService.add(chunk);
                        return ok();
                    } catch (NumberFormatException e) {
                        return badRequest(toJson(e.getMessage()));
                    }catch (NullPointerException e){
                        return badRequest("input data are incorrect: "+ctx().request().body().asJson().textValue());
                    }
                }, ec.current()
        );
    }
}
