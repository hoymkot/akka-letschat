package com.letstalk.actor;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.letstalk.actor.message.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PeopleManager extends AbstractBehavior<Command> {

    private static Behavior<Command> _instance = null;

    public static Behavior<Command> create() {
        // allow only one instance of this class
        if (_instance == null) {

            _instance = Behaviors.setup(PeopleManager::new);
        }
        return _instance;
    }

    public PeopleManager(ActorContext<Command> context) {
        super(context);
    }

    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Login.class, this::onLogin)
                .onMessage(SendMessage.class, this::onSendMessage)

                .onMessage(PersonTerminated.class, this::onPersonTerminated)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }

    private Behavior<Command> onSendMessage(SendMessage send) {
        this.usernameToActor.entrySet().stream()
                .filter(k->k.getKey().equals(send.fromUser) == false)
                .forEach(k-> {
                            ActorRef<Command> p = k.getValue();
                            Message msg = new Message(send.fromUser, send.message );
                            p.tell(msg);
                        }
                );
        return this;
    }

    private PeopleManager onPersonTerminated(PersonTerminated m) {
        this.usernameToActor.remove(m.getUsername());
        // TODO: notified friends that this person is offline now;
        return this;

    }

    private PeopleManager onLogout(Logout m) {
        return this;
    }

    private final Map<String, ActorRef<Command> > usernameToActor  = new HashMap<> ();

    private PeopleManager onLogin(Login m) {
        getContext().getLog().info("login process ");
        ActorRef<Command> person = m.getReplyTo();

        // TODO: Perform login authentication logic
        if (false ) {

            // TODO: send authentication failure to remote person
        }
        else {
            //TODO: on success Return a list of friends currently online and offline to senders

            LoginResp resp = new LoginResp(m.getUsername(), m.getRequestId());

            // TODO: find online friends
            resp.setOnlineFriends(new ArrayList());

            // TODO: lookup in database for a list friends currently offline
            resp.setAllFriends(new ArrayList<>());
            person.tell(resp);
//
            // TODO: death watch on this new Person
            getContext().watchWith(person, new PersonTerminated(m.getUsername()));


            this.usernameToActor.put(m.getUsername(), person);
            this.usernameToActor.entrySet().stream()
                    .filter(k->k.getKey().equals(m.getUsername()) == false)
                    .forEach(k-> {
                        ActorRef<Command> p = k.getValue();
                        Message msg = new Message("system", m.getUsername() + " goes online. " );
                        p.tell(msg);

                    }


            );

        }
        return this;
    }

    private Behavior<Command> onPostStop() {
        getContext().getLog().info("PeopleManager stopped");
        return this;
    }

}
