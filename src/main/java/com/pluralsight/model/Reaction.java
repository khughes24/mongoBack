package com.pluralsight.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;

public class Reaction {
    @BsonProperty("Created")
    public Date Created;
    @BsonProperty("Reaction")
    public String Reaction;
    @BsonProperty("ReactedBy")
    public Integer ReactedBy;


    Reaction(Date date,String reactionType,Integer Id){ //all three constructor
        this.Created = date;
        this.Reaction = reactionType;
        this.ReactedBy = Id;
    }

    public Reaction(String reactionType, Integer Id){ //Dateless constructor
        this.Created = new Date();
        this.Reaction = reactionType;
        this.ReactedBy = Id;
    }

    Reaction(){ //Generic constructor

    }




}
