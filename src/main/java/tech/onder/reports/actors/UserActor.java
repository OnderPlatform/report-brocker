package tech.onder.reports.actors;

import akka.Done;
import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.stream.KillSwitches;
import akka.stream.Materializer;
import akka.stream.UniqueKillSwitch;
import akka.stream.javadsl.*;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.assistedinject.Assisted;
import play.libs.Json;
import scala.concurrent.duration.Duration;
import tech.onder.consumer.models.ConsumptionChunkReport;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


/**
 * The broker between the WebSocket and the StockActor(s).  The UserActor holds the connection and sends serialized
 * JSON data to the client.
 */
public class UserActor extends AbstractActor {

    private final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private final Map<String, UniqueKillSwitch> stocksMap = new HashMap<>();

    private final String id;
    private final Materializer mat;

    private final Sink<JsonNode, NotUsed> hubSink;
    private final Flow<JsonNode, JsonNode, NotUsed> websocketFlow;
    private final ChunkReportManagementService chunkReportManagementService;

    @Inject
    public UserActor(@Assisted String id,
                     ChunkReportManagementService chunkReportManagementService,
                     Materializer mat) {
        this.id = id;
        this.chunkReportManagementService = chunkReportManagementService;
        chunkReportManagementService.subscribe(id);
        this.mat = mat;

        Pair<Sink<JsonNode, NotUsed>, Source<JsonNode, NotUsed>> sinkSourcePair =
                MergeHub.of(JsonNode.class, 16)
                        .toMat(BroadcastHub.of(JsonNode.class, 256), Keep.both())
                        .run(mat);

        this.hubSink = sinkSourcePair.first();
        Source<JsonNode, NotUsed> hubSource = sinkSourcePair.second();

        Sink<JsonNode, CompletionStage<Done>> jsonSink = Sink.ignore();

        this.websocketFlow = Flow.fromSinkAndSource(jsonSink, hubSource)
                .watchTermination((n, stage) -> {
                    // When the flow shuts down, make sure this actor also stops.
                    stage.thenAccept(f -> context().stop(self()));
                    return NotUsed.getInstance();
                });
    }

    @Override
    public void postStop() throws Exception {
        this.chunkReportManagementService.unsubscribe(this.id);
        super.postStop();
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(String.class, s -> {
                    WebsocketDTO dto = chunkReportManagementService.calculate(this.id);
                    JsonNode js = Json.toJson(dto);
                    Source<JsonNode, NotUsed> source =
                            Source.single(js);
                    // Set up a complete runnable graph from the stock source to the hub's sink
                    sender().tell(websocketFlow, self());
                }).build();
    }


    public static Props props(ActorRef out) {
        return Props.create(UserActor.class, out);
    }


    public interface Factory {
        Actor create(String id);
    }
}