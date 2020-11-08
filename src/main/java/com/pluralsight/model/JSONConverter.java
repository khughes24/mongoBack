package com.pluralsight.model;

import org.bson.Document;

public class JSONConverter {

    /**
     * Converts any doc to a json
     * @param doc
     * @return
     */
    public String convertDoctoJson(Document doc){


        String json = doc.toJson();
        return json;
    }
}
