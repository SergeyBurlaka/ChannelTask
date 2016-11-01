package com.example.kostya.channeltask.model;

/**
 * Created by kostya on 01.11.16.
 */

public class Channel {
    private String name;
    private String id;
    private String tvURL;

    public Channel() {

    }

    public Channel(String channelName, String channelId, String channelTvUrl) {
        name = channelName;
        id = channelId;
        tvURL = channelTvUrl;
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

    public String getTvURL() {
        return tvURL;
    }

    public void setTvURL(String tvURL) {
        this.tvURL = tvURL;
    }
}
