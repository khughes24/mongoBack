package com.pluralsight.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class PostClient {

    private Client client;
    public PostClient(){
        client = ClientBuilder.newClient();
    }




}
