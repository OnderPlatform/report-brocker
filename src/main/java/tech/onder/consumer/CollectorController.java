package tech.onder.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import tech.onder.consumer.models.MeterInputDTO;
import tech.onder.consumer.services.ChunkReportManagementService;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
                        logger.info(jsonNode.toString());
                        MeterInputDTO dto = Json.fromJson(jsonNode, MeterInputDTO.class);
                        chunkConverter.toChunks(dto)
                                .stream()
                                .peek(v -> logger.trace(toJson(v).toString()))
                                .forEach(chunkReportManagementService::add);
                        return ok();
                    } catch (NumberFormatException e) {
                        return badRequest(toJson(e.getMessage()));
                    } catch (RuntimeException e) {
                        return badRequest("input data is incorrect: " + ctx().request().body().asJson().textValue());
                    }
                }, ec.current()
        );
    }

    public CompletionStage<Result> pushList() {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        logger.info("load");
                        ObjectMapper mapper = new ObjectMapper();
                        List<?> meterStorage = FileUtils.readLines(new File("/home/ubuntu/report-broker/conf/meters-storage-back.json"));
                         // List<?> meterStorage = FileUtils.readLines(new File("conf/meters-storage-back.json"));
                        meterStorage.stream()
                                .map(s -> {
                                    try {
                                        return mapper.readValue((String) s, MeterInputDTO.class);
                                    } catch (IOException ioe) {
                                        return null;
                                    }

                                }).filter(Objects::nonNull)
                                .forEach(dto ->
                                        chunkConverter.toChunks(dto)
                                                .stream()
                                                .peek(v -> logger.trace(toJson(v).toString()))
                                                .forEach(chunkReportManagementService::add));
                        return Results.ok();
                    } catch (IOException ioe) {
                        return Results.badRequest();
                    }

                }
                , ec.current());
    }

}
