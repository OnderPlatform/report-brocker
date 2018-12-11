package tech.onder.reports.actors;

import akka.actor.AbstractActorWithTimers;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import tech.onder.consumer.models.WebsocketQueueItem;
import tech.onder.consumer.services.IWebsocketQueueManager;
import tech.onder.reports.ReportService;
import tech.onder.reports.models.WebsocketDTO;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * The broker between the WebSocket and the StockActor(s).  The UserActor holds the connection and sends serialized
 * JSON data to the client.
 */
public class UserActor extends AbstractActorWithTimers {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private final String id;


    private final ConcurrentLinkedDeque<WebsocketQueueItem> wsQueue = new ConcurrentLinkedDeque<>();

    private final ReportService reportService;

    private final IWebsocketQueueManager websocketQueueManager;

    public static final class Tick {
    }

    @Inject
    public UserActor(IWebsocketQueueManager websocketQueueManager, ReportService reportService) {
        this.id = UUID.randomUUID().toString();
        this.websocketQueueManager = websocketQueueManager;
        this.reportService = reportService;
        websocketQueueManager.subscribe(id, this.wsQueue);
    }


    @Override
    public void postStop() throws Exception {
        this.websocketQueueManager.unsubscribe(this.id);
        super.postStop();
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(Tick.class, message -> {
                    /** temporal solution. Current implementation  is able to handle only last item from collection*/
                    Optional<WebsocketQueueItem> opItem = Optional.ofNullable(this.wsQueue.getLast());
                    this.wsQueue.clear();
                    List<WebsocketDTO> answerList = opItem.map(reportService::websocketOutput)
                            .map(Collections::singletonList)
                            .orElseGet(Collections::emptyList);
                    JsonNode js = Json.toJson(answerList);
                    sender().tell(js, self());
                }).build();
    }
}