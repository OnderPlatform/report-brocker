package tech.onder.consumer;

import play.mvc.Controller;
import play.mvc.Result;
import tech.onder.consumer.models.ConsumptionChunkReport;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

public class CollectorController extends Controller {



    public CompletionStage<Result> push(String uuid) {

        return CompletableFuture.completedFuture(ok());
    }
}
