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
import jdk.nashorn.internal.parser.JSONParser;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Post {

    public ObjectId _id; ///this is the right type for the id
    @BsonProperty(value = "id")
    public Integer id;
    @BsonProperty(value = "author")
    public Integer author;
    @BsonProperty(value = "created")
    public Date created;
    @BsonProperty(value = "text")
    public String text;
    @BsonProperty(value = "multimedia")
    public List<String> multimedia;
    @BsonProperty(value = "distribution")
    public List<Integer> distribution;
    @BsonProperty(value = "stats")
    public Stats stats;

    @BsonProperty(value = "reaction")
    public List<Reaction> reaction;

    /**
     * A dummy constructor for all your construction needs
     */
    public Post(){

    }


    /**
     * DebugPost
     * Create a sample post for debugging purposes
     * @return
     */
    public Post debugPost(){

        this._id = new ObjectId("507f1f77bcf86cd799439011"); ///Create an object id with a default value
        this.id = 12;
        this.author = 10;
        this.created = new Date();
        this.text = "test text";

        //Gen multimedia strings
        List multi = new ArrayList<String>();
        multi.add("a Really long string to denote a url address for a giff");
        multi.add("b Really long string to denote a url address for a giff");
        this.multimedia = multi;

        //Gen distribution strings
        List distri = new ArrayList<Integer>();
        distri.add(1);
        distri.add(2);
        this.distribution = distri;

        //Gen stats
        Stats debugStats = new Stats();
        debugStats.shares = 13;
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
        debugStats.reactionCounts = react;
        this.stats = debugStats;

        Reaction reactionReply = new Reaction();
        reactionReply.Created = new Date();
        reactionReply.ReactedBy = 1;
        reactionReply.Reaction = "ANGRY";
        this.reaction.add(reactionReply);

        Comment comment = new Comment();
        comment.Created = new Date();
        comment.Author = 1;
        comment.Text = "Example comment text";
        comment.GIFLink = "http gif";
        comment.reactionCounts = react;
        List reactionList = new ArrayList<Reaction>();
        reactionList.add(reactionReply);
        comment.reaction = reactionList;
        List commentList = new ArrayList<Comment>();
        commentList.add(comment);


        Reply reply = new Reply();
        reply.Created = new Date();
        reply.Author = 4;
        reply.Text = "texty";
        reply.GIFLink = "giffy linky";
        reply.reactionCounts = react;
        reply.reactions = reactionList;
        List replyList = new ArrayList<Reply>();
        replyList.add(reply);


        return this;

    }

    /**
     * InsertPostPojo
     * Connects to the Posts database using the monoclient and some custom codecs
     * Once connected the documents are retrieved and then a new Post document is added
     * The status of the insert is returned
     *
     * @param newpost - The post post we want to add to the DB
     * @param mongo - Our mongo client which allows us to connect to the DB
     * @return
     */
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


    /**
     * GetPost
     * Connects to the DB using the mongoclient and makes a request to find a document with the id of postInt
     * This is returned as a document
     * @param postInt - The Id of the post we want from the DB
     * @param mongo - Our mongoclient to connect to the DB
     * @return
     */
    public Document getPost(Integer postInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);


        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Posts");
        Document post = collection.find(eq("id", postInt)).first();

        return post;
    }


    /**
     * GetPostList
     * Connects to the database using the mongoclient and queries the database to get all the post documents
     * These are then returned as a list
     * @param mongo - Our mongoclient to connect to the DB
     * @return
     */
    public List<Document> getPostList(MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("Posts");
        FindIterable<Document> postsIt =  collection.find();

        // Loop through our list of Iterables to create a new list of documents
        List<Document> docs = new ArrayList<Document>();
        for (Document document : postsIt) {
            docs.add(document);
        }

        return docs;

    }


    /**
     * DeletePost
     * Connects to the database using the mongoclient and queries the database to delete the requested record
     * @param postInt - The Id of the post we want to delete
     * @param mongo - Our mongoclient to connect to the DB
     * @return
     */
    public String deletePost(Integer postInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Posts");
        Bson filter = eq("Id", postInt);
        DeleteResult  result = collection.deleteOne(filter);
        System.out.println(result);
        if (result == null){
            return "Error";
        }
        return "Success";
    }

    /**
     * UpdatePost
     * Connects to the database using the mongoclient and queries the database to get the post we want to update
     * Once retrieved we send across our update request
     * @param postInt - The Id of the post we want to update
     * @param postString - The new Post string that we want to update
     * @param mongo - Our mongoclient to connect to the DB
     * @return
     */
    public String updatePost(Integer postInt, String postString,  MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);
        MongoCollection<Document> collection = database.getCollection("Posts");

        collection.updateOne(Filters.eq("id", postInt), Updates.set("text", postString));

        return "Success";
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
    public String addreaction(Integer postInt, MongoClient mongo, Reaction newReact){
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
        MongoCollection<Document> collection = database.getCollection("Posts");
        String status = "success";
        try{


            Document stats = collection.find(eq("id", postInt))
                    .projection(fields(include("stats"), excludeId())).first();
            String statsJSON = stats.toJson();
            JSONObject json = new JSONObject(statsJSON);
            String count = json.getJSONArray("stats").toString();
            JsonParser jsonParser = new JsonFactory().createParser(stats.toJson());
            ObjectMapper mapper = new ObjectMapper();
            Stats newStat = mapper.readValue(statsJSON, Stats.class);
            Bson filter = eq("id", postInt);
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
