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
     * @param newpost - The post we want to add to the DB
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

        //Check if the post we are about to add exists in the DB yet
        try{
            Post doc = this.getPost(newpost.id, mongo);
            if(doc._id.toString().equals("")){
                status = "Post already exists in DB";
                return status;
            }
        }catch (Exception ex){
            status = ex.getMessage();
            return status;
        }



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
    public Post getPost(Integer postInt, MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat").withCodecRegistry(pojoCodecRegistry);
        System.out.println("Credentials ::"+ credential);
        PostResponse postResponse = new PostResponse();

        // Retrieving a collection
        MongoCollection<Post> collection = database.getCollection("Posts" , Post.class);
        Post post = collection.find(eq("id", postInt)).first();

        //create the post response
        postResponse.id = post._id;
        postResponse.createdDateTime = new Date();
        postResponse.modifiedDateTime = new Date();
        postResponse.text = post.text;
        StatsResponse statsResponse = new StatsResponse();

        Comment comment = new Comment();
        List<Document>  commentList =  new ArrayList<Document>();
        commentList = comment.getAllComment(post.id, mongo);
        statsResponse.commentCount = commentList.size();
        statsResponse.shares = post.stats.shares;
        statsResponse.reactionCount = post.stats.reactionCounts;

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.id = commentList.get(0).getObjectId(_id);
        //commentResponse.author.username = String.valueOf(commentList.get(0).getInteger("user"));
        Document test = commentList.get(0);
        Integer testint = test.getInteger("user");
        Author tempAuthor = new Author();
        tempAuthor.username = String.valueOf(testint);
        commentResponse.author = tempAuthor;
        commentResponse.createdDateTime = new Date();
        commentResponse.createdDateTime = new Date();
        commentResponse.text = commentList.get(0).getString(text);
        commentResponse.stats = (Stats) commentList.get(0).getList(stats,Stats.class);
        postResponse.comment = commentResponse;





        return post;
    }


    /**
     * GetPostList
     * Connects to the database using the mongoclient and queries the database to get all the post documents
     * These are then returned as a list
     * @param mongo - Our mongoclient to connect to the DB
     * @return
     */
    public List<PostResponse> getPostList(MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat");
        System.out.println("Credentials ::"+ credential);

        // Retrieving a collection
        MongoCollection<Post> collection = database.getCollection("Posts", Post.class);
        FindIterable<Post> postsIt =  collection.find();

        // Loop through our list of Iterables to create a new list of documents
        List<Post> docs = new ArrayList<Post>();
        List<PostResponse> postResponses = new ArrayList<PostResponse>();
        for (Post document : postsIt) {
            PostResponse postResponse = new PostResponse();
            postResponse.id = document._id;
            postResponse.createdDateTime = new Date();
            postResponse.modifiedDateTime = new Date();
            postResponse.text = document.text;
            StatsResponse statsResponse = new StatsResponse();

            Comment comment = new Comment();
            List<Document>  commentList =  new ArrayList<Document>();
            commentList = comment.getAllComment(document.id, mongo);
            statsResponse.commentCount = commentList.size();
            statsResponse.shares = document.stats.shares;
            statsResponse.reactionCount = document.stats.reactionCounts;
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.id = commentList.get(0).getObjectId(id);
            commentResponse.author.username = commentList.get(0).getString(author);
            commentResponse.createdDateTime = new Date();
            commentResponse.createdDateTime = new Date();
            commentResponse.text = commentList.get(0).getString(text);
            commentResponse.stats = (Stats) commentList.get(0).getList(stats,Stats.class);
            postResponse.comment = commentResponse;



            postResponses.add(postResponse);
        }

        return postResponses;

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

            //Add reaction to list
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
            //End addition


            //Add to count
            Integer reactType = Integer.parseInt(newReact.Reaction);
            MongoCollection<Post> collectionPost = database.getCollection("Posts" , Post.class);
            Post post = collectionPost.find(eq("id", postInt)).first();
            post.stats.reactionCounts = this.statChanger(post.stats.reactionCounts, reactType);
            collection.updateOne(Filters.eq("id", postInt), Updates.set("stats.reactionCounts", post.stats.reactionCounts));
            //End count add

        }catch (Exception ex){
            status = ex.getMessage();
        }

        return status;
    }




    /**
     * AddReaction
     * Connects to the database using the mongoclient and queries the database to get the post we want to add to
     * Once connected we create a new reaction document and push it to the reaction list array
     * @param mongo - Our mongoclient to connect to the DB
     * @return
     */
    public String addStat(MongoClient mongo){
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
                "password".toCharArray());
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        System.out.println("Connected to the database successfully");

        Integer reactType = 7;

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("appyChat").withCodecRegistry(pojoCodecRegistry);
        System.out.println("Credentials ::"+ credential);
        String status = "";

        try{
            MongoCollection<Post> collection = database.getCollection("Posts" , Post.class);
            Post post = collection.find(eq("id", 1)).first();
            post.stats.reactionCounts = this.statChanger(post.stats.reactionCounts, reactType);
            collection.updateOne(Filters.eq("id", 1), Updates.set("stats.reactionCounts", post.stats.reactionCounts));







            status = post.text;



        }catch (Exception ex){
            status = ex.getMessage();
        }




        return status;
    }

    public ReactionCounts statChanger(ReactionCounts reactionCounts, Integer reactType){
        switch (reactType){
            case 1:
                reactionCounts.Angry ++;
                break;
            case 2:
                reactionCounts.Awesome ++;
                break;
            case 3:
                reactionCounts.Boring ++;
                break;
            case 4:
                reactionCounts.Care ++;
                break;
            case 5:
                reactionCounts.Crazy ++;
                break;
            case 6:
                reactionCounts.FakeNews ++;
                break;
            case 7:
                reactionCounts.Haha ++;
                break;
            case 8:
                reactionCounts.Lame ++;
                break;
            case 9:
                reactionCounts.Legal ++;
                break;
            case 10:
                reactionCounts.Like ++;
                break;
            case 11:
                reactionCounts.Love ++;
                break;
            case 12:
                reactionCounts.Meal ++;
                break;
            case 13:
                reactionCounts.Sad ++;
                break;
            case 14:
                reactionCounts.Scary ++;
                break;
            case 15:
                reactionCounts.Wow ++;
                break;
        }
        return reactionCounts;
    }





}
