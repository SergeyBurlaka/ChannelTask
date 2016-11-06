package com.example.kostya.channeltask.model.channel_model;

/**
 * Created by kostya on 01.11.16.
 */

public class ChannelProgram {
    private long date;
    private String showID;
    private String tvShowName;

    public ChannelProgram() {

    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getShowID() {
        return showID;
    }

    public void setShowID(String showID) {
        this.showID = showID;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }
}
