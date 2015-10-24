package com.sau.classboard.model;

import java.util.ArrayList;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushIgnore;
import co.uk.rushorm.core.annotations.RushList;

/**
 * Created by saurabh on 2015-10-24.
 */
public class BoardData extends RushObject{
    public UserData owner;

    public boolean isSelfOwner;

    public String title, code, description;
    public boolean isPrivate;

    @RushList(classType = MessageData.class)
    public ArrayList<MessageData> messages;

    @RushIgnore
    public ArrayList<UserData> members;

    public BoardData(){}
}
