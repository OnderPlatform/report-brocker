package tech.onder.core;

import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import tech.onder.core.repositories.MeterRelationRepo;
import tech.onder.core.repositories.MeterRepo;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CommonController {

    private final MeterRepo meterRepo;
    private final MeterRelationRepo meterRelationRepo;

    @Inject
    public CommonController(MeterRepo meterRepo, MeterRelationRepo meterRelationRepo) {
        this.meterRepo = meterRepo;
        this.meterRelationRepo = meterRelationRepo;
    }

    public CompletionStage<Result> meters() {
        return CompletableFuture.supplyAsync(() -> meterRepo.all()).thenApply(Json::toJson).thenApply(Results::ok);
    }

    public CompletionStage<Result> meterRelations() {
        return CompletableFuture.supplyAsync(() -> meterRelationRepo.all()).thenApply(Json::toJson).thenApply(Results::ok);

    }
}
