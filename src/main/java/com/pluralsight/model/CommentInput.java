package com.pluralsight.model;

public class CommentInput {

    public String authToken;
    public Comment comment;


    // Getters and setters
    public Comment getComment() {
        return comment;
    }
    public void setComment(Comment comment) {
        this.comment = comment;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    //Constructor
    public CommentInput(){

    }
}
