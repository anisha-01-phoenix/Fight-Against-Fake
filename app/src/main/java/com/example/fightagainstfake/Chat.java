package com.example.fightagainstfake;

public class Chat {

    private String sender, receiver, datetime, message;

    public Chat() {
    }

    public Chat(String sender, String receiver, String datetime, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.datetime = datetime;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
