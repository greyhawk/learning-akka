package com.example.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * SupervisedActor
 * @author ging wu
 * @date 2018/12/11
 */
public class SupervisedActor extends AbstractActor {

    public static Props props() {
        return Props.create(SupervisedActor.class, SupervisedActor::new);
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("supervised actor started");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("supervised actor stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("fail", f -> {
                    System.out.println("supervised actor fails now");
                    throw new Exception("I fails");
                }).build();
    }
}
