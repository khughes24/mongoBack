package com.pluralsight;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig{

    public ApplicationConfig() {
        register(MyResource.class);
    }
}
