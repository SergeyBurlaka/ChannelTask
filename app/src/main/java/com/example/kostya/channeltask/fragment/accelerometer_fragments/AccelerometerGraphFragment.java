package com.example.kostya.channeltask.fragment.accelerometer_fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kostya on 04.11.16.
 */

public class AccelerometerGraphFragment extends Fragment {
    private static final String ARG_SESSION_NAME = "ARG_SESSION_NAME";
    private List<AccelerometerData> mAccelerometerDataList;
    private String mSessionName;
    private GraphView mAccelerometerGraph;
    private LineGraphSeries<DataPoint> xSeries;
    private LineGraphSeries<DataPoint> ySeries;
    private LineGraphSeries<DataPoint> zSeries;

    public static AccelerometerGraphFragment newInstance(String sessionName) {
        AccelerometerGraphFragment fragment = new AccelerometerGraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SESSION_NAME, sessionName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSessionName = getArguments().getString(ARG_SESSION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accelerometer_graph, container, false);
        mAccelerometerGraph = (GraphView) view.findViewById(R.id.accelerometer_graph);
        initGraph();
        return view;
    }

    private void initGraph() {
        DatabaseReference reference = FirebaseHelper.getAccelerometerDataForUser(mSessionName);
        reference.orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAccelerometerDataList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AccelerometerData accelerometerData = data.getValue(AccelerometerData.class);
                    mAccelerometerDataList.add(accelerometerData);
                }
                buildGraph();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void buildGraph() {
        xSeries = new LineGraphSeries<>();
        ySeries = new LineGraphSeries<>();
        zSeries = new LineGraphSeries<>();

        for (int i = 0; i < mAccelerometerDataList.size(); i++) {
            float x = mAccelerometerDataList.get(i).getX();
            float y = mAccelerometerDataList.get(i).getY();
            float z = mAccelerometerDataList.get(i).getZ();
            String date = mAccelerometerDataList.get(i).getDate();
            Date sessionDate = getDate(date);

            appendSeriesData(x, y, z, sessionDate);
        }
        addSeries();
        initTimeAxisLabel();
    }

    private Date getDate(String date) {
        Date sessionDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        try {
            sessionDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sessionDate;
    }

    private void appendSeriesData(float x, float y, float z, Date sessionDate) {
        zSeries.appendData(createDataPoint(sessionDate, z), true, mAccelerometerDataList.size());
        ySeries.appendData(createDataPoint(sessionDate, y), true, mAccelerometerDataList.size());
        xSeries.appendData(createDataPoint(sessionDate, x), true, mAccelerometerDataList.size());
        seriesStyle();
    }

    private void seriesStyle() {
        xSeries.setColor(Color.RED);
        ySeries.setColor(Color.BLACK);
        zSeries.setColor(Color.BLUE);

        xSeries.setTitle("x");
        ySeries.setTitle("y");
        zSeries.setTitle("z");
    }

    private void addSeries() {
        mAccelerometerGraph.refreshDrawableState();
        if (mAccelerometerGraph.getSeries() != null) {
            mAccelerometerGraph.removeAllSeries();
        }
        mAccelerometerGraph.addSeries(xSeries);
        mAccelerometerGraph.addSeries(ySeries);
        mAccelerometerGraph.addSeries(zSeries);
        showPointLabel();
    }

    private void showPointLabel() {
        mAccelerometerGraph.getLegendRenderer().setVisible(true);
        mAccelerometerGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }



    private void initTimeAxisLabel() {
        GridLabelRenderer labelRenderer = mAccelerometerGraph.getGridLabelRenderer();
        labelRenderer.setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(),
                new SimpleDateFormat("HH:mm:ss")));
    }

    private DataPoint createDataPoint(Date date, float axis) {
        return new DataPoint(date, axis);
    }
}
