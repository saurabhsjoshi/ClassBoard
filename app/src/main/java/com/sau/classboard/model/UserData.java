package com.sau.classboard.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushIgnore;

/**
 * Created by saurabh on 2015-10-24.
 */
public class UserData extends RushObject{
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public int type;

    @RushIgnore
    public String password;


    @RushIgnore
    public static UserData currentUser = null;

    public static UserData getCurrentUser(Context context){
        if(currentUser == null)
            currentUser = new UserData(context);

        return currentUser;
    }

    public UserData(){}

    public UserData(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        firstName = prefs.getString("FIRST_NAME","");
        lastName = prefs.getString("LAST_NAME", "");
        email = prefs.getString("EMAIL","");
        password = prefs.getString("PASSWORD", password);

    }

}
