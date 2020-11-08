package com.pluralsight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.pluralsight.model.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Path("system")
public class UserResource {

    /**
     * createToken
     * Creates a token from the userId if the userid is valid
     * @return
     */
    @Path("/token")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response createToken(String userId){
        User user = new User();
        String token = user.createToken(userId);
        if(token.length() == 0){
            return Response.status(Response.Status.NOT_FOUND).entity("").build();


        }else{
            return Response.ok().entity(token).build();
        }
    }

}
