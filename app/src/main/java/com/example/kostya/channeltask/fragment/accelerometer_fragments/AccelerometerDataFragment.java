package com.example.kostya.channeltask.fragment.accelerometer_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.acc_holders.AccelerometerDataHolder;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

public class AccelerometerDataFragment extends Fragment {
    private static final String ARG_SESSION_NAME = "ARG_SESSION_NAME";
    private static final String TODAYS_DATE = "04/11/2016";

    private String mSessionName;

    public static AccelerometerDataFragment newInstance(String sessionName) {
        AccelerometerDataFragment fragment = new AccelerometerDataFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAccelerometerDataList(recyclerView);
        return view;
    }

    private void initAccelerometerDataList(RecyclerView recyclerView) {
        DatabaseReference reference = FirebaseHelper.getDataFromSession(mSessionName);

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AccelerometerData, AccelerometerDataHolder>(AccelerometerData.class,
                R.layout.fragment_accelerometer_item, AccelerometerDataHolder.class,
                reference.orderByChild("date").startAt(TODAYS_DATE)) {

            @Override
            protected void populateViewHolder(AccelerometerDataHolder accelerometerDataHolder, AccelerometerData accelerometerData, int position) {
                accelerometerDataHolder.setX(accelerometerData.getX());
                accelerometerDataHolder.setY(accelerometerData.getY());
                accelerometerDataHolder.setZ(accelerometerData.getZ());
                accelerometerDataHolder.setDate(accelerometerData.getDate());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
}
