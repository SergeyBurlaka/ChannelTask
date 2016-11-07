package com.example.kostya.channeltask.holder.acc_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kostya.channeltask.R;

import java.util.Date;

/**
 * Created by kostya on 04.11.16.
 */

public class AccelerometerDataHolder extends RecyclerView.ViewHolder {
    public View mView;

    public AccelerometerDataHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setX(float x) {
        TextView xDataTextView = (TextView) mView.findViewById(R.id.x_data);
        String xDate = Float.toString(x);
        xDataTextView.setText(xDate);
    }

    public void setY(float y) {
        TextView yDataTextView = (TextView) mView.findViewById(R.id.y_data);
        yDataTextView.setText(Float.toString(y));
    }

    public void setZ(float z) {
        TextView zDataTextView = (TextView) mView.findViewById(R.id.z_data);
        zDataTextView.setText(Float.toString(z));
    }

    public void setDate(String date) {
        TextView dateTextView = (TextView) mView.findViewById(R.id.acc_date);
        dateTextView.setText(date);
    }
}
