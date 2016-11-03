package com.example.kostya.channeltask.model;

/**
 * Created by kostya on 02.11.16.
 */

public class User {
    private String mEmail;
    private String mName;

    public User() {

    }

    public User(String email, String name) {
        mEmail = email;
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
