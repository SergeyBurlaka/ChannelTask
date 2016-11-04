package com.example.kostya.channeltask.fragment.accelerometer_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.example.kostya.channeltask.prefs.PrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 04.11.16.
 */

public class AccelerometerGraphFragment extends Fragment {
    private List<AccelerometerData> mAccelerometerDataList;
    private GraphView mAccelerometerGraph;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accelerometer_graph, container, false);
        mAccelerometerGraph = (GraphView) view.findViewById(R.id.accelerometer_graph);
        getAxis();
        return view;
    }

    private void getAxis() {
        String uniqueId = PrefManager.getPrefManager().getUniqueUser(getContext());
        int sessionId = PrefManager.getPrefManager().getLastSessionNumber(getContext());

        DatabaseReference reference = FirebaseHelper.getAccelerometerDataForUser(uniqueId, sessionId);


        reference.orderByChild("x").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAccelerometerDataList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AccelerometerData accelerometerData = data.getValue(AccelerometerData.class);
                    mAccelerometerDataList.add(accelerometerData);
                }
                initAxis();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initAxis() {
        LineGraphSeries<DataPoint> dataPointLineGraphSeries = new LineGraphSeries<>();
        for (int i = 0; i < mAccelerometerDataList.size(); i++) {
            float x = mAccelerometerDataList.get(i).getX();
            float y = mAccelerometerDataList.get(i).getY();
            dataPointLineGraphSeries.appendData(initDataPoint(x, y), true, mAccelerometerDataList.size());
        }
        mAccelerometerGraph.addSeries(dataPointLineGraphSeries);
    }

    private DataPoint initDataPoint(float x, float y) {
        return new DataPoint(x, y);
    }
}
