package com.akkademy;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.akkademy.actors.AkkademyDb;

/**
 * App
 * @author ging wu
 * @date 2018/12/5
 */
public class App {

    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("akkademy");
        system.actorOf(Props.create(AkkademyDb.class), "akkademy-db");
    }
}
