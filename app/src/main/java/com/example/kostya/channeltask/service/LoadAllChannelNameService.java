package com.example.kostya.channeltask.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.example.kostya.channeltask.holder.ChannelProgramHolder;
import com.example.kostya.channeltask.model.ChannelList;
import com.example.kostya.channeltask.model.ChannelProgram;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoadAllChannelNameService extends IntentService {
    public static final String ACTION_START_LOAD = "ACTION_START_LOAD";

    public LoadAllChannelNameService() {
        super("Service");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getAction().equals(ACTION_START_LOAD)) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Program")
                    .child("2016Jul08");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot channelSnapShot : dataSnapshot.getChildren()) {
                        ChannelProgram program = channelSnapShot.getValue(ChannelProgram.class);
                        String showID = program.getShowID();
                        ChannelList.getsChannelList().addChannel(showID);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
