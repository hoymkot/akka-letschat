package com.letstalk.actor.message;

public class ManagerTerminated implements Command {
    private String username;
    public ManagerTerminated(String username){
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

}
