package com.example.kostya.channeltask.holder.acc_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kostya.channeltask.R;

/**
 * Created by kostya on 07.11.16.
 */

public class SessionHolder extends RecyclerView.ViewHolder{
    public View mView;


    public SessionHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDate(String date) {
        TextView dateTextView =(TextView) mView.findViewById(R.id.acc_date);
        dateTextView.setText(date);
    }

    public void setOnSessionClickListener(View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }
}
