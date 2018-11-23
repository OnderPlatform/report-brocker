package tech.onder.reports;


import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.streams.ActorFlow;
import tech.onder.reports.actors.UserActor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashSet;
import java.util.Set;

@Singleton
public class Publisher{
    private final int queueCapacity;
    private final Set<ActorRef> actorRefs;

    @Inject
    public Publisher(UserActor.Factory childFactory) {
        this.queueCapacity = 256;
        this.actorRefs = new LinkedHashSet<>();
        this.childFactory = childFactory;
    }
    private final UserActor.Factory childFactory;

    public Source<JsonNode, ?> register() {
        Source<JsonNode, ?> source = Source.<JsonNode>actorRef(this.queueCapacity, OverflowStrategy.dropHead())
                .mapMaterializedValue(a -> {
                    Publisher.this.actorRefs.add(a);
                    return a;
                })
                .watchTermination((a, termination) -> {
                    termination.whenComplete((done, cause) -> {
                        Publisher.this.actorRefs.remove(a);
                    });
                    return null;
                });
        return source;
    }

    public void broadcast(final JsonNode message) {
        for (ActorRef actorRef : this.actorRefs) {
            actorRef.tell(message, ActorRef.noSender());
        }
    }

    public void clear() {
        for (ActorRef actorRef : this.actorRefs) {
            actorRef.tell(PoisonPill.getInstance(), ActorRef.noSender());
        }
        this.actorRefs.clear();
    }
}

