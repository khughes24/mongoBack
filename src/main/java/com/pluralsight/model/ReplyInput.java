package com.pluralsight.model;

public class ReplyInput {

    public String authToken;
    public Reply reply;

    // Getters and setters
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public Reply getReply() {
        return reply;
    }
    public void setReply(Reply reply) {
        this.reply = reply;
    }

    // Constructor
    public ReplyInput(){

    }
}
