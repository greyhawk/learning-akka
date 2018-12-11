package com.akkademy.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LastStringTest {

    private ActorSystem system = ActorSystem.create();

    @Test
    public void itShouldLastMessage() {
        TestActorRef<LastString> actorRef = TestActorRef.create(system, Props.create(LastString.class));
        actorRef.tell("Hello", ActorRef.noSender());
        LastString lastString = actorRef.underlyingActor();
        assertThat(lastString.getLast()).isEqualTo("Hello");
        actorRef.tell("world", ActorRef.noSender());
        actorRef.tell("tiger", ActorRef.noSender());
        assertThat(lastString.getLast()).isEqualTo("tiger");
    }

}
