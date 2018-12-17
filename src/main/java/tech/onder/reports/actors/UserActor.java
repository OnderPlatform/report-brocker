package tech.onder.reports.actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.Actor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import tech.onder.queue.models.WebsocketQueueItem;
import tech.onder.reports.models.dto.WebsocketDTO;
import tech.onder.reports.services.ReportService;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;


public class UserActor extends AbstractActorWithTimers {
    
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    
    private final String id;
    
    private final ArrayBlockingQueue<WebsocketQueueItem> wsQueue;
    
    private final ReportService reportService;
    
    private final IWebsocketQueuePublisher websocketQueueManager;
    
    public static final class Tick {
    
    }
    
    @Inject
    public UserActor(IWebsocketQueuePublisher websocketQueueManager, ReportService reportService) {
        this.id = UUID.randomUUID().toString();
        this.websocketQueueManager = websocketQueueManager;
        this.reportService = reportService;
        this.wsQueue = new ArrayBlockingQueue<>(20);
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
//                           List<WebsocketDTO> answerList = new ArrayList<>();
                           WebsocketQueueItem item = this.wsQueue.poll();
                           while (!this.wsQueue.isEmpty()) {
                               item = this.wsQueue.poll();
//                               WebsocketDTO wsDto = reportService.websocketOutput(item);
//                               answerList.save(wsDto);
                           }
                    
                           /** temporal solution. Current client is not  able to handle collections*/
                           this.wsQueue.offer(item);
                           WebsocketDTO wsDto = reportService.websocketOutput(item);
                           JsonNode js = Json.toJson(wsDto);
                           sender().tell(js, self());
                       }).build();
    }
    public interface Factory {
        Actor create(String id);
    }
    
    
}