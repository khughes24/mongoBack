package com.pluralsight.model;

public class PostInput {


    public String authToken;
    public Post post;

    //Getters and setters
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    //Constructor
    public PostInput(){

    }
}
