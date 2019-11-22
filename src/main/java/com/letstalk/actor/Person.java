package com.letstalk.actor;


import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;
import com.letstalk.actor.message.*;
import scala.util.Using;

import java.util.*;

public class Person extends AbstractBehavior<Command> implements Command{
    public Person(ActorContext<Command> context, String username) {
        super(context);
        this.username = username;
    }

    public static Behavior<Command> create(String username) {
        return Behaviors.setup(context->new Person(context, username));
    }


    @Override
    public Receive createReceive() {
        return newReceiveBuilder()
                .onMessage(LoginResp.class, this::onLoginResp)
                .onMessage(Message.class, this::onMessage)
                .onMessage(SendMessage.class, this::onSendMessage)
                .onMessage(ManagerTerminated.class, this::onManagerTerminated)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private  Behavior<Command> onManagerTerminated(ManagerTerminated m) {

        // TODO: Do something when manager failed.
        return this;
    }

    private  Behavior<Command> onSendMessage(SendMessage m) {
        peopleManager.tell(m);
        return this;
    }

    private Behavior<Command> onMessage(Message m) {
        getContext().getLog().info(m.fromUser + " says: " + m.message);
        return this;
    }

    private Person onLogin(Login m) {
        //TODO: register itself to PeopleManager and pass login credential to PeopleManager
        return this;
    }
    ;
    private Boolean isOnline = false;
    private ActorRef<Command> peopleManager = null;
    private String username = null;

    private Person onLoginResp(LoginResp m) {
        getContext().getLog().info("login response process ");
        this.isOnline = true;
        return this;
    }

    public Boolean isOnline(){
        getContext().getLog().info("online? " + this.isOnline);
        return this.isOnline;
    }

    private Person onPostStop() {
        return this;
    }
}
