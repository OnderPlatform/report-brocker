package tech.onder.consumer.services;

import tech.onder.consumer.models.WebsocketQueueItem;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IWebsocketQueueManager {

    void subscribe(String aClientId, ConcurrentLinkedDeque<WebsocketQueueItem> aWebSocketQueue);

    void unsubscribe(String aClientId);
}
