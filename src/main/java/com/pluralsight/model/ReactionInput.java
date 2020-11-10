package com.pluralsight.model;

public class ReactionInput {

    public String authToken;
    public Reaction reaction;


    // Getters and setters
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public Reaction getReaction() {
        return reaction;
    }
    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }


    // Constructor
    public ReactionInput(){

    }



}
