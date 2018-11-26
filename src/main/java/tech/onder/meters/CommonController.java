package tech.onder.meters;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import play.mvc.Results;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.repositories.MeterRelationRepo;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.reports.models.ReportDTO;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Controller.ctx;

public class CommonController {

    private final MeterRepo meterRepo;

    private final MeterRelationRepo meterRelationRepo;

    private final MeterService meterService;

    private final HttpExecutionContext ec;

    @Inject
    public CommonController(MeterRepo meterRepo, MeterRelationRepo meterRelationRepo, MeterService meterService, HttpExecutionContext ec) {
        this.meterRepo = meterRepo;
        this.meterRelationRepo = meterRelationRepo;
        this.meterService = meterService;
        this.ec = ec;
    }

    public CompletionStage<Result> meters() {
        return CompletableFuture.supplyAsync(() -> meterRepo.all()).thenApply(Json::toJson).thenApply(Results::ok);
    }

    public CompletionStage<Result> addMeter() {
        return CompletableFuture.supplyAsync(() -> ctx().request().body().asJson(), ec.current())
                .thenApply(jo -> Json.fromJson(jo, MeterInputDTO.class))
                .thenAccept(meterService::add)
                .thenApply((s) -> Results.ok());
    }

    public CompletionStage<Result> meterRelations() {
        return CompletableFuture.supplyAsync(() -> meterRelationRepo.all()).thenApply(Json::toJson).thenApply(Results::ok);

    }

}
