package com.pluralsight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.pluralsight.model.*;
import io.swagger.annotations.Api;
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

@Path("posts")
public class MyResource {


    /**
     * GetIt
     * Simple test to see if the connection is working
     * @return
     */
    @Path("/test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(){
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        return "Got it";
    }

    //-------------post---------------------------------------------

    /**
     * GetPost
     * Creates a new post object and then calls getpost to get the requested post
     * @param postId - The post we want to get
     * @return
     */
    @GET
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam ("postId") String postId) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");
        if(postId.equals("")){
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }

        Post post = new Post();
        Document docs = post.getPost(Integer.valueOf(postId), mongoClient);
        JSONConverter converter = new JSONConverter();
        String json = "";

        json = json + converter.convertDoctoJson(docs);
        if(docs.size() >0 ){
            return Response.ok().entity(json).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * GetPostList
     * Creates a new post object and then calls getPostList to get all the posts
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostList() {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");
        Post post = new Post();
        List<Document> docs = post.getPostList( mongoClient);
        JSONConverter converter = new JSONConverter();
        String json = "";
        for(Document d : docs){
            json = json + converter.convertDoctoJson(d);
        }
        if(docs.size() >0 ){
            return Response.ok().entity(json).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * AddPost
     * Creates a new post object and then calls insertPostPojo add the post to the mongo post DB
     * @param newPost - The new post we want to add
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPost(Post newPost) {

        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");
        Post post = new Post();
        String docs = post.insertPostPojo(newPost, mongoClient);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * PostDelete
     * Creates a new post object and then calls deletePost to remove the requested post
     * @param postId - The new post we want to add
     * @return
     */
    @DELETE
    @Path("/{postId}")
    public Response postDelete(@PathParam ("postId") String postId) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Post post = new Post();
        String docs = post.deletePost(Integer.valueOf(postId), mongoClient);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * PostUpdate
     * Creates a new post object and then calls updatePost to update the requested post
     * @param postId - The new post we want to add
     * @param newText - The new post text we want to update the DB with
     * @return
     */
    @POST
    @Path("/{postId}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response postUpdate(@PathParam ("postId") String postId, String newText) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Post post = new Post();
        String docs = post.updatePost(Integer.valueOf(postId),newText, mongoClient);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }

    }

    /**
     * PostReactions
     * Creates a new post object and then calls addreaction to update the requested post with the new reaction
     * @param postId - The new post we want to add
     * @param newReact - The new reaction we want to add to the post
     * @return
     */
    @POST
    @Path("/{postId}/reactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postReaction(@PathParam ("postId") Integer postId, Reaction newReact) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Post post = new Post();
        Reaction remakeReact = new Reaction(newReact.Reaction,newReact.ReactedBy);
        String docs = post.addreaction(Integer.valueOf(postId), mongoClient,remakeReact);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }

    }



    //----------------------------------------
    //Comment
    //----------------------------------------

    /**
     * dbgComments
     * Creates a debug comment which is returned to the user
     * @return
     */
    @GET
    @Path("/comments/dbg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dbgComments() {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Comment comment = new Comment();
        Comment docs = comment.debugComment();
        String str = docs.toString();
        JSONConverter converter = new JSONConverter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(docs);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
            return Response.ok().entity(json).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();


        }
        return Response.ok().entity(str).build();

    }


    /**
     * GetComments
     * Gets all comments for the parsed postId
     * This is returned as a list to the user
     * @param postId - The postId for the requested comments
     * @return
     */
    @GET
    @Path("/{postId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetComments(@PathParam ("postId") String postId) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Comment comment = new Comment();
        List<Document> docs = comment.getAllComment(Integer.valueOf(postId), mongoClient);
        JSONConverter converter = new JSONConverter();
        String json = "";
        for(Document d : docs){
            json = json + converter.convertDoctoJson(d);
        }
        if(docs.size() >0 ){
            return Response.ok().entity(json).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * addComment
     * Add a new comment to a postId
     * Returns the status of the insert
     * @param postId - The postId of the post we want to add a comment to
     * @param newComment - The new comment we want to add to the post
     * @return
     */
    @POST
    @Path("/{postId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam ("postId") String postId, Comment newComment) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Comment comment = new Comment();
        String docs = comment.addComment(newComment, mongoClient);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }


    /**
     * updateComment
     * Updates a target comment of a target post with the parsed text
     * Returns the status of the update
     * @param postId - The postId of the post we want to work with
     * @param commentId - The commentId of the comment we want to update
     * @param newText - The new text to update
     * @return
     */
    @POST
    @Path("/{postId}/comments/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateComment(@PathParam ("postId") String postId,
                                  @PathParam ("commentId") String commentId, String newText) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Comment comment = new Comment();
        String docs = comment.updateComment(Integer.valueOf(commentId),newText,mongoClient);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * deleteComment
     * Deletes the target comment
     * Returns the status of the deletion
     * @param postId - The postId of the post we want to work with
     * @param commentId - The commentId of the comment we want to delete
     * @return
     */
    @DELETE
    @Path("/{postId}/comments/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@PathParam ("postId") String postId,@PathParam ("commentId") String commentId) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Comment comment = new Comment();
        String docs = comment.deleteComment(Integer.valueOf(commentId),mongoClient);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * commentReaction
     * Adds a new reaction to the target comment
     * Returns the status of the add
     * @param postId - The postId of the post we want to work with
     * @param commentId - The commentId of the comment we want to add the new reaction to
     * @param newReact - The new reaction we want to add to
     * @return
     */
    @POST
    @Path("/{postId}/comments/reactions/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response commentReaction(@PathParam ("postId") Integer postId,@PathParam ("commentId") String commentId, Reaction newReact) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Comment comment = new Comment();
        Reaction remakeReact = new Reaction(newReact.Reaction,newReact.ReactedBy);
        String docs = comment.addreaction(Integer.valueOf(postId),Integer.valueOf(commentId), mongoClient,remakeReact);

        if(docs.equals("SUCCESS") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }

    }






    //--------------------------------------------------------
    //Reply stuff
    //----------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * dbgReply
     * Generates a dbg reply which can be used for testing
     * Returns the dbg reply (the same each time)
     * @return
     */
    @GET
    @Path("/reply/dbg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response dbgReply() {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Reply reply = new Reply();
        Reply docs = reply.debugReply();
        String str = docs.toString();
        JSONConverter converter = new JSONConverter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(docs);
            System.out.println("ResultingJSONstring = " + json);
            return Response.ok().entity(json).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();


        }
        return Response.ok().entity(str).build();

    }


    /**
     * GetReplies
     * Gets a list of replies for a certain comment in a post
     * Returns the list of replies
     * @param postId - The postId for the post we want to work with
     * @param commentId - The commentId for the comment we want to get replies for
     * @return
     */
    @GET
    @Path("/{postId}/comments/{commentId}/replies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetReplies(@PathParam ("postId") String postId,@PathParam ("commentId") String commentId) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Reply reply = new Reply();
        List<Document> docs = reply.getReply(Integer.valueOf(commentId), mongoClient);
        JSONConverter converter = new JSONConverter();
        String json = "";
        for(Document d : docs){
            json = json + converter.convertDoctoJson(d);
        }
        if(docs.size() > 0 ){
            return Response.ok().entity(json).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * addReply
     * Adds a reply to the target comment of a post
     * Returns the status of the add
     * @param postId -The postId for the post we want to work with
     * @param commentId - The commentId for the comment we want to get replies for
     * @param newReply - The new reply we want to add to the comment
     * @return
     */
    @POST
    @Path("/{postId}/comments/{commentId}/replies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReply(@PathParam ("postId") String postId,@PathParam ("commentId") String commentId, Reply newReply) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Reply reply = new Reply();
        String docs = reply.addReply(newReply, mongoClient);

        if(docs.equals("Success")){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * updateReply
     * @param postId - The postId of the post we want to work with
     * @param commentId - The commentId of the comment we want to work with
     * @param replyId - The replyId of the reply we want to edit
     * @param newString - The new string for the reply
     * @return
     */
    @POST
    @Path("/{postId}/comments/{commentId}/replies/{replyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReply(@PathParam ("postId") String postId,@PathParam ("commentId") String commentId,@PathParam ("replyId") String replyId, String newString) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Reply reply = new Reply();
        String docs = reply.updateReply(Integer.valueOf(replyId),newString,mongoClient);

        if(docs.equals("Success")){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * deleteReply
     * Deletes the target reply of a comment of a post
     * Returns the status of the delete
     * @param postId - The postId of the post we want to work with
     * @param commentId - The commentId of the comment we want to work with
     * @param replyId - The replyId of the reply we want to delete
     * @return
     */
    @DELETE
    @Path("/{postId}/comments/{commentId}/replies/{replyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReply(@PathParam ("postId") String postId,@PathParam ("commentId") String commentId,@PathParam ("replyId") String replyId) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Reply reply = new Reply();
        String docs = reply.deleteReply(Integer.valueOf(replyId),mongoClient);

        if(docs.equals("Success")){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
    }

    /**
     * replyReaction
     * Adds a reaction to a specific reply
     * Returns the status of the addition
     * @param postId - The postId of the post we want to work with
     * @param commentId The commentId of the comment we want to work with
     * @param replyId - The replyId of the reply we want to work with
     * @param newReact - The new reaction we want to add to the reply
     * @return
     */
    @POST
    @Path("/{postId}/comments/{commentId}/replies/reactions/{replyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response replyReaction(@PathParam ("postId") Integer postId,@PathParam ("commentId") String commentId,@PathParam ("replyId") String replyId, Reaction newReact) {
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        User user = new User();
        user.authorize("TEMPTOKEN");

        Reply reply = new Reply();
        Reaction remakeReact = new Reaction(newReact.Reaction,newReact.ReactedBy);
        String docs = reply.addreaction(Integer.valueOf(postId),Integer.valueOf(commentId),Integer.valueOf(replyId), mongoClient,remakeReact);

        if(docs.equals("Success") ){
            return Response.ok().entity(docs).build();

        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }

    }
}