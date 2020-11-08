package com.pluralsight.client;

import com.pluralsight.model.Activity;
import com.pluralsight.model.Post;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class PostClient {

    private Client client;
    public PostClient(){
        client = ClientBuilder.newClient();
    }




}
