package com.example.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * StartStopActor2
 * @author ging wu
 * @date 2018/12/11
 */
public class StartStopActor2 extends AbstractActor {

    public static Props props() {
        return Props.create(StartStopActor2.class, StartStopActor2::new);
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("second started");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("second stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
