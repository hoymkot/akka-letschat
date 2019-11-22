package com.letstalk.actor.message;

import akka.actor.typed.ActorRef;

import java.util.UUID;

public class Message implements Command {
    public String fromUser;
    public String message;


    public Message(String fromUser, String message) {
        this.fromUser = fromUser;
        this.message = message;
    }


}
