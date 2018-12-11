package com.akkademy.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import com.akkademy.messages.SetRequest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AkkaademyDbTest {

    private ActorSystem system = ActorSystem.create();

    @Test
    public void itShouldPlaceKeyValueFromSetRequestMessageIntoMap() {
        TestActorRef<AkkademyDb> actorRef = TestActorRef.create(system, Props.create(AkkademyDb.class));
        actorRef.tell(new SetRequest("key", "value"), ActorRef.noSender());
        AkkademyDb akkademyDb = actorRef.underlyingActor();
        assertThat(akkademyDb.getMap().get("key")).isEqualTo("value");
    }

}
