package com.example.geoswitch;

import android.util.Log;

public class User {

    public static String email;
    public static String username;

    public User(String email, String username) {
        User.email = email;
        User.username = username;
    }

    public String getEmail() {
        return email;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }
}


