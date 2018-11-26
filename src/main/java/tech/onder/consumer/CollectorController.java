package tech.onder.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import tech.onder.consumer.models.MeterInputDTO;
import tech.onder.consumer.services.ChunkReportManagementService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;

public class CollectorController extends Controller {

    private final Logger logger = LoggerFactory.getLogger(CollectorController.class);

    private final HttpExecutionContext ec;

    private final ChunkReportManagementService chunkReportManagementService;

    private final ChunkConverter chunkConverter;

    @Inject
    public CollectorController(HttpExecutionContext ec, ChunkReportManagementService chunkReportManagementService, ChunkConverter chunkConverter) {
        this.ec = ec;
        this.chunkReportManagementService = chunkReportManagementService;
        this.chunkConverter = chunkConverter;
    }

    public CompletionStage<Result> push(String uuid) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        JsonNode jsonNode = ctx().request().body().asJson();
                        logger.trace(jsonNode.toString());
                        MeterInputDTO dto = Json.fromJson(jsonNode, MeterInputDTO.class);
                        chunkConverter.toChunks(dto)
                                .stream()
                                .peek(v -> logger.info(toJson(v).toString()))
                                .forEach(chunkReportManagementService::add);
                        return ok();
                    } catch (NumberFormatException e) {
                        return badRequest(toJson(e.getMessage()));
                    } catch (NullPointerException e) {
                        return badRequest("input data are incorrect: " + ctx().request().body().asJson().textValue());
                    }
                }, ec.current()
        );
    }

}
