package com.letstalk.actor;

import akka.actor.typed.ActorSystem;
import com.letstalk.actor.PeopleManager;
import com.letstalk.actor.Person;
import com.letstalk.actor.message.Command;
import com.letstalk.actor.message.Login;
import com.letstalk.actor.message.SendMessage;
import com.lightbend.akka.sample.GreeterMain;

import java.io.IOException;

public class LetsChat {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Started");
        final ActorSystem<Command> manager = ActorSystem.create(PeopleManager.create(), "LetsChat");


        final ActorSystem<Command> person1 = ActorSystem.create(Person.create("user1"), "user1");

        final ActorSystem<Command> person2 = ActorSystem.create(Person.create("user2"), "user2");
        final ActorSystem<Command> person3 = ActorSystem.create(Person.create("user3"), "user3");

        Login login = new Login("user1", person1);

        manager.tell(login);
        login = new Login("user2", person2);
        manager.tell(login);
        login = new Login("user3", person3);
        manager.tell(login);

        manager.tell(new SendMessage("user1", "Hello Everyone!"));

        System.out.println("Ended ");


    }
}
