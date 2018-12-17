package tech.onder.reports.actors;

import tech.onder.queue.models.WebsocketQueueItem;

import java.util.concurrent.ArrayBlockingQueue;

public interface IWebsocketQueuePublisher {
    
    /**
     * Subscribe for updating of the queue
     *
     * @param aClientId
     * @param aWebSocketQueue
     */
    void subscribe(String aClientId, ArrayBlockingQueue<WebsocketQueueItem> aWebSocketQueue);
    
    void feedWebsocketQueues();
    
    /**
     * Remove queue from subscriptions
     *
     * @param aClientId
     */
    void unsubscribe(String aClientId);
    
}
