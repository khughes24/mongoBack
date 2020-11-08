package com.pluralsight.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.push;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Reply {

    public ObjectId _id; ///this is the right type for the id
    @BsonProperty(value = "id")
    public Integer id;
    @BsonProperty(value = "commentId")
    public Integer commentId;
    public Date Created;
    public Integer Author;
    public String Text;
    public String GIFLink;
    @BsonProperty(value = "reactionCounts")
    public ReactionCounts reactionCounts;
    public List<Reaction> reactions;

    //Dummy constructor
    public Reply(){

    }

    //validate method incoming
    public String validateOutGoing(){
        return "a";
    }



    //Returns a sample post to be used for easier debugginh
    public Reply debugReply(){

        ReactionCounts react = new ReactionCounts();
        react.Angry = 0;
        react.Awesome = 0;
        react.Boring = 8;
        react.Care = 0;
        react.Crazy = 0;
        react.FakeNews = 0;
        react.Haha = 0;
        react.Lame = 0;
        react.Legal = 0;
        react.Like = 0;
        react.Love = 0;
        react.Meal = 0;
        react.Sad = 1;
        react.Scary = 0;
        react.Wow = 0;

        Reaction reactionReply = new Reaction();
        reactionReply.Created = new Date();
        reactionReply.ReactedBy = 1;
        reactionReply.Reaction = "ANGRY";
        List reactionList = new ArrayList<Reaction>();
        reactionList.add(reactionReply);

        Reply reply = new Reply();
        reply._id = new ObjectId("507f1f77bcf86cd799439033"); ///Create an object id with a default value
        reply.id = 70;
        reply.commentId = 1;
        reply.Created = new Date();
        reply.Author = 4;
        reply.Text = "texty";
        reply.GIFLink = "giffy linky";
        reply.reactionCounts = react;
        reply.reactions = reactionList;

        return reply;

    }

    public String deleteReply(Integer replyInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Replys");
        Bson filter = eq("id", replyInt);
        DeleteResult result = collection.deleteOne(filter);
        System.out.println(result);
        if (result.getDeletedCount() < 1){
            return "Error";
        }
        return "Success";
    }


    public String reactToReply(Integer replyInt, String reactionId, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Replys");
        Document reply = collection.find(eq("id", replyInt)).first();
        Reaction reaction = new Reaction(reactionId, 1);
        //reply.reaction.add(reaction);
        ///ReactionCounts reactionCounts = new ReactionCounts();
        //reply.reactionCounts = reactionCounts.updateReactionCounts(reply.reactionCounts, reactionId);

        //System.out.println(result);
        //if (result == null){
          //  return "Error";
        //}
        return "Success";
    }

    public String updateReply(Integer replyInt, String replyString,  MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Replys");

        collection.updateOne(Filters.eq("id", replyInt), Updates.set("Text", replyString));
        return "Success";
    }

    public String  addReply(Reply newReply, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat").withCodecRegistry(pojoCodecRegistry);
        System.out.println("Credentials ::"+ credential);

        Person ada = new Person();
        // Retrieving a collection
        String status = "OK";
        try{
            MongoCollection<Reply> collection = database.getCollection("Replys" , Reply.class);

            collection.withCodecRegistry(pojoCodecRegistry);
            collection.countDocuments();
            collection.insertOne(newReply);
        }catch (Exception ex){
            status = ex.getMessage();
        }

        return status;
    }

    public List<Document> getReply(Integer commentId, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Replys");
        FindIterable<Document> postsIt =  collection.find(eq("commentId", commentId));
        List<Document> docs = new ArrayList<Document>();
        for (Document document : postsIt) {
            docs.add(document);
            System.out.println(document);
        }

        return docs;
    }



    public String updateReplyText( MongoClient mongo, Integer replyId, String text){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Replys");
        Document reply = collection.find(eq("id", replyId)).first();
        reply.append("text", text);
        //collection.insertOne(reply);

        return "OK";
    }

    public String addReplyReaction( MongoClient mongo, Integer replyId, Reaction reaction){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Replys");
        Document reply = collection.find(eq("id", replyId)).first();
        List<Reaction> reactionList = (List<Reaction>) reply.get("reaction");
        reactionList.add(reaction);
        reply.append("reaction", reaction);
        //collection.insertOne(reply);

        return "OK";
    }

    /**
     * AddReaction
     * Connects to the database using the mongoclient and queries the database to get the post we want to add to
     * Once connected we create a new reaction document and push it to the reaction list array
     * @param postInt - The Id of the post we want to update
     * @param mongo - Our mongoclient to connect to the DB
     * @param newReact - The new reaction we want to add
     * @return
     */
    public String addreaction(Integer postInt,Integer commentInt,Integer replyInt, MongoClient mongo, Reaction newReact){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat").withCodecRegistry(pojoCodecRegistry);
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Replys");
        String status = "success";
        try{


            Document stats = collection.find(eq("id", replyInt))
                    .projection(fields(include("reactionCounts"), excludeId())).first();
            String statsJSON = stats.toJson();
            JSONObject json = new JSONObject(statsJSON);
            //String count = json.getJSONArray("stats").toString();
            JsonParser jsonParser = new JsonFactory().createParser(stats.toJson());
            ObjectMapper mapper = new ObjectMapper();
            Stats newStat = mapper.readValue(statsJSON, Stats.class);
            Bson filter = eq("id", replyInt);
            Document react = new Document().append("Created", newReact.Created)
                    .append("Reaction",newReact.Reaction)
                    .append("ReactedBy",newReact.ReactedBy);
            Bson updateOperation = push("reactions", react);
            UpdateOptions options = new UpdateOptions().upsert(true);
            UpdateResult updateResult = collection.updateOne(filter, updateOperation, options);
        }catch (Exception ex){
            status = ex.getMessage();
        }

        /**
         * public String addreaction(Integer postInt, MongoClient mongo, Reaction newReact){
         *         MongoCredential credential;
         *         credential = MongoCredential.createCredential("sampleUser", "myDb",
         *                 "password".toCharArray());
         *         CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
         *                 fromProviders(PojoCodecProvider.builder().automatic(true).build()));
         *         System.out.println("Connected to the database successfully");
         *
         *         // Accessing the database
         *         MongoDatabase database = mongo.getDatabase("appyChat").withCodecRegistry(pojoCodecRegistry);
         *         System.out.println("Credentials ::"+ credential);
         *
         *
         *         // Retrieving a collection
         *         MongoCollection<Document> collection = database.getCollection("Posts");
         *         String status = "success";
         *         try{
         *             Document react = new Document().append("Created", newReact.Created)
         *                     .append("Reaction",newReact.Reaction)
         *                     .append("ReactedBy",newReact.ReactedBy);
         *             collection.find(exists("reaction")).first();
         *             collection.updateOne(eq("_id", "postInt"),Updates.addToSet("reaction", react));
         *             collection.updateOne(eq("_id", "postInt").
         *         }catch (Exception ex){
         *             status = ex.getMessage();
         *         }
         *
         *
         *
         *
         *
         *
         */





        return status;
    }






}
