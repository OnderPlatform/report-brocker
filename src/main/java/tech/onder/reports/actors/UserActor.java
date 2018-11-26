package tech.onder.reports.actors;

import akka.Done;
import akka.NotUsed;
import akka.actor.AbstractActorWithTimers;
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
import com.google.inject.assistedinject.AssistedInject;
import play.libs.Json;
import scala.concurrent.duration.Duration;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


/**
 * The broker between the WebSocket and the StockActor(s).  The UserActor holds the connection and sends serialized
 * JSON data to the client.
 */
public class UserActor extends AbstractActorWithTimers {

    private final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private final Map<String, UniqueKillSwitch> stocksMap = new HashMap<>();

    private final String id;
    private final Materializer mat;

//    private final Sink<JsonNode, NotUsed> hubSink;
//    private final Flow<JsonNode, JsonNode, NotUsed> websocketFlow;
    private final ChunkReportManagementService chunkReportManagementService;
    private static Object TICK_KEY = "TickKey";

    public static final class FirstTick {
    }

    public static final class Tick {
    }

    @Inject
    public UserActor(ChunkReportManagementService chunkReportManagementService,
                     Materializer mat) {
        this.id = UUID.randomUUID().toString();
        this.chunkReportManagementService = chunkReportManagementService;
        chunkReportManagementService.subscribe(id);
        this.mat = mat;
    }


    @Override
    public void postStop() throws Exception {
        this.chunkReportManagementService.unsubscribe(this.id);
        super.postStop();
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(FirstTick.class, message -> {
                    // do something useful here
                    getTimers().startPeriodicTimer(TICK_KEY, new Tick(), Duration.create(5, TimeUnit.SECONDS));

                })
                .match(Tick.class, message -> {
                   WebsocketDTO dto = chunkReportManagementService.calculate(this.id);
                    JsonNode js = Json.toJson(dto);
                    sender().tell(js, self());
                }).build();
    }



    public interface Factory {
        Actor create(ChunkReportManagementService chunkReportManagementService);
    }

}