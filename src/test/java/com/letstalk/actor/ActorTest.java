package com.letstalk.actor;

import akka.actor.testkit.typed.javadsl.*;
import akka.actor.typed.*;
import com.letstalk.actor.message.*;
import hou.kot.iot.Device;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ActorTest {

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

//    @Test
    public void testPeopleLogin() {
//        ActorRef<Command> peopleManager = testKit.spawn(PeopleManager.create());
//        TestProbe<LoginResp> person = testKit.createTestProbe(LoginResp.class);
//        Login login = new Login("user", person.getRef());
//        peopleManager.tell(login);
////        LoginResp resp = person.receiveMessage();
////        assertEquals(login.getRequestId(), resp.getRequestId());

    }
}