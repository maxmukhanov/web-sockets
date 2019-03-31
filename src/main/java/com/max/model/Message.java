package com.max.model;

public class Message {

    private String from;
    private String content;

    public Message() {

    }
    public Message(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public String getContent() {
        return content;
    }
}
