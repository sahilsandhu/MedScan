package com.learning.medscan;

public class MessageMember {

    String Message,sender,reciever;

    public MessageMember(){}

    public String getMessage() {
        return Message;
    }

    public void setMessage(String name) {
        this.Message = name;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }
}
