package com.sample.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class Message {

    private final String id;
    private final String title;
    private final String message;

    public Message(@JsonProperty("id") final String id,
                   @JsonProperty("title") final String title,
                   @JsonProperty("message") final String message
                   ) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"title\":\"" + title + "\"," +
                "\"message\":\"" + message + "\""+
                "}";
    }
}
