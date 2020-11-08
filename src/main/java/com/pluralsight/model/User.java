package com.pluralsight.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean authorize(String token){

        // Authorization logic will go here
        // But for now just return true
        return true;
    }


    public String createToken(String id){

        // Creates a token which will be used in the authorization process
        // But for now just return a random string
        String token = "AAAAAAAAAA";
        return token;
    }
}
