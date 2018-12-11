package com.akkademy.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * LastString
 * @author ging wu
 * @date 2018/12/4
 */
public class LastString extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private String last;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
            logger.info("Received message {} ", message);
            last = message;
        }).matchAny(message -> logger.info("Received unknown message {}", message))
                .build();
    }

    public String getLast() {
        return last;
    }
}
