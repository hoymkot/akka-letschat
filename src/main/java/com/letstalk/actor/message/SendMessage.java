package com.letstalk.actor.message;

public class SendMessage implements Command {
    public String fromUser;
    public String message;


    public SendMessage(String fromUser, String message) {
        this.fromUser = fromUser;
        this.message = message;
    }


}
