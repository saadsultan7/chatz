package com.example.chatz.Models;

public class messagemodel {
    String uid , message;
    Long timestamp;
    public messagemodel(String uid , String message , Long timestamp)
    {
        this.uid = uid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public messagemodel(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }
    public messagemodel(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
