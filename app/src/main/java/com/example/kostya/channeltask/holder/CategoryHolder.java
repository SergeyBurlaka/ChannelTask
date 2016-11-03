package com.example.kostya.channeltask.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kostya.channeltask.R;

/**
 * Created by kostya on 01.11.16.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {
    public View mView;

    public CategoryHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView channelName = (TextView) mView.findViewById(R.id.channel_name);
        channelName.setText(name);
    }

    public void setOnCategoryClickListener(View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }
}
