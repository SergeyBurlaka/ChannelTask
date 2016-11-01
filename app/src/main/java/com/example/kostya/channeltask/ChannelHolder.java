package com.example.kostya.channeltask;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kostya on 01.11.16.
 */

public class ChannelHolder extends RecyclerView.ViewHolder {
    private View mView;

    public ChannelHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView channelName = (TextView) mView.findViewById(R.id.channel_name);
        channelName.setText(name);
    }
}
