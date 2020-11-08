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

public class Comment {

    public ObjectId _id; ///this is the right type for the id
    @BsonProperty(value = "id")
    public Integer id;
    @BsonProperty(value = "postId")
    public Integer postId;
    public Date Created;
    public Integer Author;
    public String Text;
    public String GIFLink;
    @BsonProperty(value = "reactionCounts")
    public ReactionCounts reactionCounts;
    public List<Reaction> reaction;


    //Dummy constructor
    public Comment(){

    }

    //validate method incoming
    public String validateOutGoing(){
        return "a";
    }


    //Returns a sample comment to be used for easier debugginh
    public Comment debugComment(){

        Reaction reactionReply = new Reaction();
        reactionReply.Created = new Date();
        reactionReply.ReactedBy = 1;
        reactionReply.Reaction = "ANGRY";
        this.reaction = reaction;

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

        Comment comment = new Comment();
        comment.Created = new Date();
        comment.Author = 1;
        comment.Text = "Example comment text";
        comment.GIFLink = "http gif";
        comment.reactionCounts = react;
        comment._id = new ObjectId("507f1f77bcf86cd799439022"); ///Create an object id with a default value
        comment.id = 70;
        comment.postId = 1;
        List reactionList = new ArrayList<Reaction>();
        reactionList.add(reactionReply);
        comment.reaction = reactionList;


        Reply reply = new Reply();
        reply.Created = new Date();
        reply.Author = 4;
        reply.Text = "texty";
        reply.GIFLink = "giffy linky";
        reply.reactionCounts = react;
        reply.reactions = reactionList;
        List replyList = new ArrayList<Reply>();
        replyList.add(reply);

        return comment;
    }

    public String deleteComment(Integer commentInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Comments");
        Bson filter = eq("Id", commentInt);
        DeleteResult result = collection.deleteOne(filter);
        System.out.println(result);
        if (result == null){
            return "Error";
        }
        return "Success";
    }


    public String updatePost(Integer postInt, String postString,  MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Posts");
        Document query = new Document();
        query.append("Id","postInt");
        Document setData = new Document();
        setData.append("post", postString);
        Document update = new Document();
        update.append("$set", setData);
        //To update single Document
        collection.updateOne(query, update);
        return "Success";
    }


    //Updates the comment text to the passed variable
    public String updateComment(Integer commentInt, String commentString, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Comments");
        collection.updateOne(Filters.eq("id", commentInt), Updates.set("Text", commentString));
        return "Success";
    }

    public List<Document> getComment(Integer commentInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Comments");
        FindIterable<Document> postsIt =  collection.find(eq("id", commentInt));
        List<Document> docs = new ArrayList<Document>();
        for (Document document : postsIt) {
            docs.add(document);
            System.out.println(document);
        }

        return docs;
    }


    public List<Document> getPostList(Integer postInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Posts");
        FindIterable<Document> postsIt =  collection.find(eq("id", postInt));
        List<Document> docs = new ArrayList<Document>();
        for (Document document : postsIt) {
            docs.add(document);
            System.out.println(document);
        }


        return docs;

    }



    public List<Document>  getAllComment(Integer postInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Comments");
        FindIterable<Document> commentsIt =  collection.find();
        List<Document> docs = new ArrayList<Document>();
        for (Document document : commentsIt) {
            docs.add(document);
            System.out.println(document);
        }

        return docs;
    }

    public String insertPostPojo(Post newpost, MongoClient mongo){
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
            MongoCollection<Post> collection = database.getCollection("Posts" , Post.class);

            collection.withCodecRegistry(pojoCodecRegistry);
            collection.countDocuments();
            collection.insertOne(newpost);
        }catch (Exception ex){
            status = ex.getMessage();
        }


        return status;
    }

    public String  addComment(Comment newComment, MongoClient mongo){
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
            MongoCollection<Comment> collection = database.getCollection("Comments" , Comment.class);

            collection.withCodecRegistry(pojoCodecRegistry);
            collection.countDocuments();
            collection.insertOne(newComment);
        }catch (Exception ex){
            status = ex.getMessage();
        }

        return status;
    }


    public String  addReply(Integer commentInt,  MongoClient mongo, Reply reply){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Comments");
        Document comment = collection.find(eq("id", commentInt)).first();
        comment.append("reply", reply);
        //collection.insertOne(comment);
        // Retrieving a collection

        return "ok";
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
    public String addreaction(Integer postInt, Integer commentInt,MongoClient mongo, Reaction newReact){
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
        MongoCollection<Document> collection = database.getCollection("Comments");
        String status = "success";
        try{


            Document stats = collection.find(eq("id", commentInt))
                    .projection(fields(include("reactionCounts"), excludeId())).first();
            String statsJSON = stats.toJson();
            JSONObject json = new JSONObject(statsJSON);
            //String count = json.getJSONArray("reactionCounts").toString();
            JsonParser jsonParser = new JsonFactory().createParser(stats.toJson());
            ObjectMapper mapper = new ObjectMapper();
            Stats newStat = mapper.readValue(statsJSON, Stats.class);
            Bson filter = eq("id", commentInt);
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
