package com.example.kostya.channeltask.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by kostya on 02.11.16.
 */

public class ChannelList {
    private static ChannelList sChannelList;
    private static HashSet<String> mChannelName;
    private static List<String> mChannelProgramName;

    private ChannelList() {

    }

    public static ChannelList getsChannelList() {
        if (sChannelList == null) {
            sChannelList = new ChannelList();
            mChannelName = new HashSet<>();
            mChannelProgramName = new ArrayList<>();
        }
        return sChannelList;
    }


    public void addChannel(String name) {
        mChannelName.add(name);
    }

    private void convertSetToList() {
        Iterator<String> iterator = mChannelName.iterator();
        while (iterator.hasNext()) {
            mChannelProgramName.add(iterator.next().toString());
        }
    }

    public List<String> getChannelName() {
        convertSetToList();
        return mChannelProgramName;
    }

    public static int getChannelSize() {
        return (mChannelName != null && mChannelName.size() != 0) ?
                mChannelName.size() : 0;
    }


}
