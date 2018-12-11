package com.example.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * StartStopActor1
 * @author ging wu
 * @date 2018/12/11
 */
public class StartStopActor1 extends AbstractActor {

    public static Props props() {
        return Props.create(StartStopActor1.class, StartStopActor1::new);
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("first started");
        getContext().actorOf(StartStopActor2.props(), "second");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("first stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("stop", s -> getContext().stop(getSelf()))
                .build();
    }

}
