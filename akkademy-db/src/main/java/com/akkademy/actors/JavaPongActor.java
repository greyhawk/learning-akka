package com.akkademy.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;

/**
 * JavaPongActor
 * @author ging wu
 * @date 2018/12/4
 */
public class JavaPongActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Ping", s -> sender().tell("Pong", ActorRef.noSender()))
                .matchAny(s -> sender().tell(new Status.Failure(new Exception("unknown message")), self()))
                .build();
    }
}
