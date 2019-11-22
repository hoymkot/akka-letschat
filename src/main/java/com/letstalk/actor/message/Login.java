package com.letstalk.actor.message;

import akka.actor.typed.ActorRef;
import com.letstalk.actor.Person;

import java.util.UUID;

public class Login implements Command {
    private String username;
    private ActorRef<Command> replyTo;
    private String requestId;


    public Login(String username, ActorRef<Command> replyTo){
        this.username = username;
        this.replyTo = replyTo;
        this.requestId = UUID.randomUUID().toString();
    }


    public String getUsername() {
        return this.username;
    }

    public ActorRef<Command> getReplyTo() {
        return replyTo;
    }

    public String getRequestId() {
        return requestId;
    }
}
