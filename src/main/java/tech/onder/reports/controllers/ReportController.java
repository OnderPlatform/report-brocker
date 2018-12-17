package tech.onder.reports.controllers;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.WebSocket;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.FiniteDuration;
import tech.onder.reports.actors.UserActor;
import tech.onder.reports.models.dto.MeterReportDTO;
import tech.onder.reports.models.dto.ReportDTO;
import tech.onder.reports.services.ReportService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

public class ReportController extends Controller {
    
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ReportController.class);
    
    private final Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
    
    private final ReportService reportService;
    
    private final ActorRef userActor;
    
    @Inject
    public ReportController(@Named("userActor") ActorRef userActor, ReportService reportService) {
        this.reportService = reportService;
        this.userActor = userActor;
    }
    
    public CompletionStage<Result> consumption() {
        List<ReportDTO> report = reportService.getConsumptionReport();
        return CompletableFuture.completedFuture(report).thenApply(Json::toJson).thenApply(Results::ok);
    }
    
    public CompletionStage<Result> prices() {
        List<ReportDTO> report = reportService.getPriceReport();
        return CompletableFuture.completedFuture(report).thenApply(Json::toJson).thenApply(Results::ok);
    }
    
    public CompletionStage<Result> meters() {
        List<MeterReportDTO> report = reportService.getMeters();
        return CompletableFuture.completedFuture(report).thenApply(Json::toJson).thenApply(Results::ok);
        
    }
    
    public CompletionStage<Result> lastConsumption(Integer aOffset) {
        Integer size = reportService.getConsumptionReport().size();
        Integer shift = (aOffset != null) ? aOffset + 1 : 1;
        ReportDTO reportDTO = new ReportDTO();
        if (size != 0) {
            reportDTO = reportService.getConsumptionReport().get(size - shift);
        }
        return CompletableFuture.completedFuture(reportDTO).thenApply(Json::toJson).thenApply(Results::ok);
    }
    
    public WebSocket ws() {
        return WebSocket.Json.accept(requestHeader -> {
            Sink<JsonNode, ?> sink = Sink.ignore();
            FiniteDuration interval = FiniteDuration.create(5, TimeUnit.SECONDS);
            
            Source<JsonNode, ?> source = Source.tick(interval, interval, NotUsed.getInstance())
                                               .mapAsync(5, e -> FutureConverters.toJava(ask(userActor, new UserActor.Tick(), timeout)))
                                               .map(e -> (JsonNode) e);
            Flow<JsonNode, JsonNode, ?> flow = Flow.fromSinkAndSource(sink, source);
            return flow;
        });
    }
    
}
