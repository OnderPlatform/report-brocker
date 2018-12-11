package tech.onder.reports.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.util.Timeout;
import play.libs.akka.InjectedActorSupport;
import tech.onder.consumer.services.IWebsocketQueueManager;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class UserParentActor extends AbstractActor implements InjectedActorSupport {

    private final Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

    public static class Create {
        final String id;

        public Create(String id) {
            this.id = id;
        }
    }

    private final UserActor.Factory childFactory;

    private final IWebsocketQueueManager IWebsocketQueueManager;
    @Inject
    public UserParentActor(UserActor.Factory childFactory, IWebsocketQueueManager IWebsocketQueueManager) {
        this.childFactory = childFactory;
        this.IWebsocketQueueManager = IWebsocketQueueManager;

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserParentActor.Create.class, create -> {
                    ActorRef child = injectedChild(() -> childFactory.create(IWebsocketQueueManager), "userActor-" + create.id);
                    CompletionStage<Object> future = ask(child, "time", timeout);
                    pipe(future, context().dispatcher()).to(sender());
                }).build();
    }

}
