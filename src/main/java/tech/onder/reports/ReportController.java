package tech.onder.reports;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import tech.onder.reports.actors.UserParentActor;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.ReportDTO;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class ReportController extends Controller {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ReportController.class);

    private final Timeout timeout = new Timeout(1, TimeUnit.SECONDS);


    private final ActorRef userParentActor;

    private final ReportService reportService;

    @Inject
    public ReportController(@Named("userParentActor") ActorRef userParentActor, ReportService reportService) {
        this.userParentActor = userParentActor;
        this.reportService = reportService;
    }

    public CompletionStage<Result> consumption() {
        List<ReportDTO> report = reportService.getComsumptionReport();
        return CompletableFuture.completedFuture(report).thenApply(Json::toJson).thenApply(Results::ok);
    }

    public CompletionStage<Result> prices() {
        List<ReportDTO> report = reportService.getPriceReport();
        return CompletableFuture.completedFuture(report).thenApply(Json::toJson).thenApply(Results::ok);
    }

    public CompletableFuture<Result> meters() {
        List<MeterReportDTO> report = reportService.getMeters();
        return CompletableFuture.completedFuture(report).thenApply(Json::toJson).thenApply(Results::ok);

    }

    public static String uuidGen() {
        return UUID.randomUUID().toString();
    }

    public WebSocket ws() {
        return WebSocket.Text.accept(requestHeader -> {
            Sink<String, ?> sink = Sink.ignore();
            Source<String, ?> source = Par.publisher.register();
            Flow<String, String, ?> flow = Flow.fromSinkAndSource(sink, source);
            return flow;
        });
        return WebSocket.Json.acceptOrResult(request -> {
            final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = wsFutureFlow(request);
            final CompletionStage<F.Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = future.thenApply(F.Either::Right);
            return stage.exceptionally(this::logException);
        });
    }

    private F.Either<Result, Flow<JsonNode, JsonNode, ?>> logException(Throwable throwable) {
        logger.error("Cannot create websocket", throwable);
        Result result = Results.internalServerError("error");
        return F.Either.Left(result);
    }

    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> wsFutureFlow(Http.RequestHeader request) {
        UserParentActor.Create create = new UserParentActor.Create(uuidGen());
        return ask(userParentActor, create, timeout).thenApply((Object flow) -> {
            final Flow<JsonNode, JsonNode, NotUsed> f = (Flow<JsonNode, JsonNode, NotUsed>) flow;
            return f.named("websocket");
        });
    }
}
