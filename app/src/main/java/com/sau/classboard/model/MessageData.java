package com.sau.classboard.model;

import com.google.gson.Gson;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushIgnore;

/**
 * Created by saurabh on 2015-10-22.
 */
public class MessageData extends RushObject {

    @RushIgnore
    public static int BOARD_MESSAGE = 0,
            PRIVATE_MESSAGE = 1, PRIVATE_INVITE = 2;

    public String message;

    public boolean isRead = false;

    public int messageType;

    public UserData sender;

    public MessageData(){}

    public MessageData(String message, String name, String email, int type){
        sender = new UserData();
        this.message = message;
        sender.email = email;
        sender.username = name;
        sender.type = type;
    }

    public void setSenderUsernName(String name){
        sender.username = name;
    }

    public void setSenderEmail(String email){
        sender.email = email;
    }

    public void setType(int type){
        sender.type = type;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getSenderEmail(){
        return sender.email;
    }

    public String getSenderUsername(){
        return sender.username;
    }

    public int getSenderType(){
        return sender.type;
    }

    public String getMessage(){
        return message;
    }

    public static String getMessageDataJSON(MessageData data){
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
