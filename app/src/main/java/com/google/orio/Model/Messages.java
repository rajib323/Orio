package com.google.orio.Model;

import java.sql.Time;

public class Messages {
    private String sender;
    private String receiver;
    private String message;



    public Messages(String sender,String receiver,String message){
        this.sender=sender;
        this.receiver=receiver;
        this.message=message;
    }

    public Messages(){

    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

}
