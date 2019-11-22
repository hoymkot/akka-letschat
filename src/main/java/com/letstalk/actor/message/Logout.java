package com.letstalk.actor.message;

public class Logout implements Command {
    private String username;
    public Logout(String username){
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

}
