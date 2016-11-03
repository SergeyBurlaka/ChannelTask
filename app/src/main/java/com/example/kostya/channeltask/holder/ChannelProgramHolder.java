package com.example.kostya.channeltask.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kostya.channeltask.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kostya on 01.11.16.
 */

public class ChannelProgramHolder extends RecyclerView.ViewHolder {
    private View mView;

    public ChannelProgramHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDate(long date) {
        TextView programDate = (TextView) mView.findViewById(R.id.program_channel_date);
        programDate.setText(countDate(date));
    }

    public void setTvShowName(String tvShowName) {
        TextView programTvShowName = (TextView) mView.findViewById(R.id.program_channel_tvShowName);
        programTvShowName.setText(tvShowName);
    }

    private String countDate(long date) {
        Date expire = new Date(date);
        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
        return simpleDate.format(expire);
    }
}

