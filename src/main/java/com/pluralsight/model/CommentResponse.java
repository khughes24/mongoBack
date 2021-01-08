package com.pluralsight.model;

import org.bson.types.ObjectId;

import java.util.Date;

public class CommentResponse {

    public ObjectId id;
    public Date createdDateTime;
    public Date modifiedDateTime;
    public String text;
    public Author author;
    public Stats stats;
}
