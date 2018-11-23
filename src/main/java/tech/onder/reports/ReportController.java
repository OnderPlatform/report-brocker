package tech.onder.reports;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import play.libs.F;
import play.libs.Json;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.FiniteDuration;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.reports.actors.UserActor;
import tech.onder.reports.actors.UserParentActor;
import tech.onder.reports.models.MeterReportDTO;
import tech.onder.reports.models.ReportDTO;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

public class ReportController extends Controller {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ReportController.class);

    private final Timeout timeout = new Timeout(5, TimeUnit.SECONDS);


    private final ActorSystem actorSystem;

    private final Materializer materializer;

    private final ReportService reportService;

    private final Publisher publisher;

    private final ChunkReportManagementService chunkReportManagementService;

    private final ActorRef userParentActor;

    @Inject
    public ReportController(@Named("userActor") ActorRef userParentActor, ActorSystem actorSystem, Materializer materializer, ReportService reportService, Publisher publisher, ChunkReportManagementService chunkReportManagementService) {
        this.actorSystem = actorSystem;
        this.userParentActor= userParentActor;
        this.materializer = materializer;
        this.reportService = reportService;
        this.publisher = publisher;
        this.chunkReportManagementService = chunkReportManagementService;
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
//        return WebSocket.Json.acceptOrResult(request -> {
//            final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = wsFutureFlow(request);
//            final CompletionStage<F.Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = future.thenApply(F.Either::Right);
//            return stage.exceptionally(this::logException);
//        });
        return WebSocket.Json.accept(requestHeader -> {
            Sink<JsonNode, ?> sink = Sink.ignore();
            //UserActor actor =new UserActor(chunkReportManagementService, materializer);
            FiniteDuration interval = FiniteDuration.create(5, TimeUnit.SECONDS);

            Source<JsonNode, ?> source =Source.tick(interval, interval, NotUsed.getInstance())
                    .mapAsync(5,e-> FutureConverters.toJava(ask(userParentActor, new UserActor.Tick(), timeout)))
                    .map(e->(JsonNode)e);
            Flow<JsonNode, JsonNode, ?> flow = Flow.fromSinkAndSource(sink, source);
            return flow;
        });
    }

//    private F.Either<Result, Flow<JsonNode, JsonNode, ?>> logException(Throwable throwable) {
//        logger.error("Cannot create websocket", throwable);
//        Result result = Results.internalServerError("error");
//        return F.Either.Left(result);
//    }
//
//    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> wsFutureFlow(Http.RequestHeader request) {
//        UserParentActor.Create create = new UserParentActor.Create(uuidGen());
//        return ask(userParentActor, create, timeout).thenApply((Object flow) -> {
//            final Flow<JsonNode, JsonNode, NotUsed> f = (Flow<JsonNode, JsonNode, NotUsed>) flow;
//            return f.named("websocket");
//        });
//    }
}
