package tech.onder.meters;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import play.mvc.Results;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.repositories.MeterRelationRepo;
import tech.onder.meters.repositories.MeterRepo;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static play.mvc.Controller.ctx;
import static play.mvc.Results.ok;

public class CommonController {

    private final MeterService meterService;

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
        return asJsonResult(meterService::all);
    }

    public CompletionStage<Result> addMeter() {
        return CompletableFuture.supplyAsync(() -> ctx().request().body().asJson(), ec.current())
                .thenApply(jo -> Json.fromJson(jo, MeterInputDTO.class))
                .thenAccept(meterService::add)
                .thenApply((s) -> ok());
    }

    public CompletionStage<Result> meterRelations() {
        return asJsonResult(meterService::allRelations);

    }

    public <T> CompletionStage<Result> asJsonResult(Supplier<T> aValueSupplier) {
        CompletableFuture.supplyAsync(aValueSupplier)
                .thenApply(Json::toJson)
                .thenApply(Results::ok);
    }

}
