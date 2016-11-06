package com.example.kostya.channeltask.holder.channel_holder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.kostya.channeltask.R;

import java.util.List;

/**
 * Created by kostya on 01.11.16.
 */

public class ChannelHolder extends RecyclerView.ViewHolder {
    public View mView;

    public ChannelHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView channelName = (TextView) mView.findViewById(R.id.channel_name);
        channelName.setText(name);
    }

    public void highlight(boolean highlight) {
        mView.setBackgroundColor(highlight ? Color.YELLOW : Color.WHITE);
    }
}
