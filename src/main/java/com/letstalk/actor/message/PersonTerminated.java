package com.letstalk.actor.message;

public class PersonTerminated implements Command {
    private String username;
    public PersonTerminated(String username){
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

}
