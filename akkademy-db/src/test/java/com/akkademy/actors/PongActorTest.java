package com.akkademy.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.Test;
import scala.concurrent.Future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static org.assertj.core.api.Assertions.assertThat;
import static scala.compat.java8.FutureConverters.toJava;

public class PongActorTest {

    private ActorSystem system = ActorSystem.create();

    private ActorRef actorRef = system.actorOf(Props.create(JavaPongActor.class));

    @Test
    public void shouldReplyToPingWithPong() throws Exception {
        Future sFuture = ask(actorRef, "Ping", 1000);
        final CompletionStage<String> cs = toJava(sFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture)cs;
        assertThat(jFuture.get(1000, TimeUnit.MILLISECONDS)).isEqualTo("Pong");
    }

    @Test(expected = ExecutionException.class)
    public void shouldReplyToUnknownMessageWithFailure() throws Exception {
        Future sFuture = ask(actorRef, "unknown", 1000);
        final CompletionStage<String> cs = toJava(sFuture);
        final CompletableFuture<String> jFuture = (CompletableFuture)cs;
        jFuture.get(1000, TimeUnit.MILLISECONDS);
    }
}
