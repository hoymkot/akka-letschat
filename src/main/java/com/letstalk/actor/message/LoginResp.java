package com.letstalk.actor.message;

import akka.actor.typed.ActorRef;
import com.letstalk.actor.Person;

import java.util.*;


// TODO: if Person doesn't receive this message on timeout, report login failure to user.


public class LoginResp implements Command {
    private String username;
    private List<String> onlineFriends = new ArrayList<>();
    private List<String> allFriends = new ArrayList<>();
    private String requestId;


    public LoginResp(String username, String requestId){
        this.username = username;
        this.requestId = requestId;
    }



    public String getUsername() {
        return this.username;
    }

    public List<String> getOnlineFriends() {
        return onlineFriends;
    }

    public void setOnlineFriends(List<String> onlineFriends) {
        this.onlineFriends = onlineFriends;
    }

    public List<String> getAllFriends() {
        return allFriends;
    }

    public void setAllFriends(List<String> allFriends) {
        this.allFriends = allFriends;
    }

    public String getRequestId() {
        return requestId;
    }
}
