package com.example.kostya.channeltask.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kostya.channeltask.R;

/**
 * Created by kostya on 01.11.16.
 */

public class ChannelProgramHolder extends RecyclerView.ViewHolder {
    private View mView;

    public ChannelProgramHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }



    public void setDate(String date) {
        TextView programDate = (TextView) mView.findViewById(R.id.program_channel_date);
//        programDate.setText(date);
    }

    public void setShowId(String showId) {
        TextView programShowId = (TextView) mView.findViewById(R.id.program_channel_name);
        programShowId.setText(showId);
    }

    public void setTvShowName(String tvShowName) {
        TextView programTvShowName = (TextView) mView.findViewById(R.id.program_channel_tvShowName);
        programTvShowName.setText(tvShowName);
    }
}

