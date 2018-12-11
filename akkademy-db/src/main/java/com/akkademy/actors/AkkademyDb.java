package com.akkademy.actors;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.akkademy.messages.GetRequest;
import com.akkademy.messages.KeyNotFoundException;
import com.akkademy.messages.SetRequest;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * AkkademyDb 数据库角色
 * @author ging wu
 * @date 2018/12/3
 */
public class AkkademyDb extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(context().system(), this);

    private final Map<String, Object> map = new HashMap<>();

    Map<String, Object> getMap() {
        return ImmutableMap.copyOf(map);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SetRequest.class, message -> {
                    log.info("Received Set request: {}", message);
                    map.put(message.getKey(), message.getValue());
                    sender().tell(new Status.Success(message.getKey()), self());
                })
                .match(GetRequest.class, message -> {
                    log.info("Received Get request: {}", message);
                    Object value = map.get(message.getKey());
                    Object response = (value != null) ? value : new Status.Failure(new KeyNotFoundException(message.getKey()));
                    sender().tell(response, self());
                })
                .matchAny(o -> {
                    log.info("Received unknown message: {}", o);
                    sender().tell(new Status.Failure(new ClassNotFoundException()), self());
                })
                .build();
    }
}
