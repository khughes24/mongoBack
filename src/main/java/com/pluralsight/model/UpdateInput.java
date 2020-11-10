package com.pluralsight.model;

public class UpdateInput {

    public String authToken;
    public String updateString;

    // Getters and setters
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String getUpdateString() {
        return updateString;
    }
    public void setUpdateString(String updateString) {
        this.updateString = updateString;
    }

    // Constructor
    public UpdateInput(){
    }



}
