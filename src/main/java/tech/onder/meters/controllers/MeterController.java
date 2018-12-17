package tech.onder.meters.controllers;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import play.mvc.Results;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.services.MeterService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static play.mvc.Controller.ctx;
import static play.mvc.Results.ok;

public class MeterController {
    
    private final MeterService meterService;
    
    private final HttpExecutionContext ec;
    
    @Inject
    public MeterController(MeterService meterService, HttpExecutionContext ec) {
        this.meterService = meterService;
        this.ec = ec;
    }
    
    public CompletionStage<Result> list() {
        return asJsonResult(meterService::all);
    }
    
    public CompletionStage<Result> add() {
        return CompletableFuture.supplyAsync(() -> ctx().request().body().asJson(), ec.current())
                                .thenApply(jo -> Json.fromJson(jo, MeterInputDTO.class))
                                .thenAccept(meterService::add)
                                .thenApply((s) -> ok());
    }
    
    public CompletionStage<Result> meterRelations() {
        return asJsonResult(meterService::allRelations);
        
    }
    
    protected <T> CompletionStage<Result> asJsonResult(Supplier<T> aValueSupplier) {
        return CompletableFuture.supplyAsync(aValueSupplier)
                                .thenApply(Json::toJson)
                                .thenApply(Results::ok);
    }
    
}
