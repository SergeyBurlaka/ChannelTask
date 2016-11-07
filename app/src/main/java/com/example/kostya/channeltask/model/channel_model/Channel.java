package com.example.kostya.channeltask.model.channel_model;

/**
 * Created by kostya on 01.11.16.
 */

public class Channel {
    private String name;
    private String id;

    public Channel() {

    }

    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
