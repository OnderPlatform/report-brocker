package tech.onder.queue.actors;

import akka.actor.AbstractActorWithTimers;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import tech.onder.queue.manager.ConsumptionQueuesManager;
import tech.onder.reports.actors.IWebsocketQueuePublisher;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;


public class ForceUpdate extends AbstractActorWithTimers {
    
    public static final class FirstTick {
    
    }
    
    public static final class Tick {
    
    }
    
    
    private IWebsocketQueuePublisher consumptionQueuesManager;
    
    @Inject
    public ForceUpdate(IWebsocketQueuePublisher consumptionQueuesManager) {
        this.consumptionQueuesManager = consumptionQueuesManager;
        getTimers().startSingleTimer("TickKey", new FirstTick(), Duration.create(1, TimeUnit.SECONDS));
    }
    
    @Override
    public Receive createReceive() {
        
        return receiveBuilder()
                       .match(FirstTick.class,
                               message -> {
                            
                                   getTimers().startPeriodicTimer("TickKey", new Tick(), FiniteDuration.create(5, TimeUnit.SECONDS));
                               })
                       .match(Tick.class, message -> {
                    
                               consumptionQueuesManager.feedWebsocketQueues();
                    
                           // do something useful here
                       })
                       .build();
    }
    
}
