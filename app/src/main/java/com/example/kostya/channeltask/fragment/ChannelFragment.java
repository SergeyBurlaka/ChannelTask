package com.example.kostya.channeltask.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.Prefs.PrefManager;
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.Channel;
import com.example.kostya.channeltask.model.ChannelList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChannelFragment extends Fragment {
    private List<String> mFaveList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.channel_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        initChannelList(mRecyclerView);

        checkFaveChannels();
        return view;
    }

    private void initChannelList() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Channels");

        final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, reference) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {
                if (mFaveList != null) {
                    checkFaveChannels(channelHolder, channel);
                }

                channelHolder.setName(channel.getName());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void checkFaveChannels(ChannelHolder channelHolder, Channel channel) {
        for (int i = 0; i < mFaveList.size(); i++) {
            if (TextUtils.equals(mFaveList.get(i), channel.getId())) {
                channelHolder.setBackground();
                break;
            } else {
                channelHolder.setWhiteColor();
            }
        }
    }


    private void checkFaveChannels() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference()
                .child("faves")
                .child(PrefManager.getPrefManager().getUniqueUser(getContext()));

        getFaveChannels(reference);

    }

    private void getFaveChannels(DatabaseReference reference) {


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Channel faveChannel = data.getValue(Channel.class);
                    String showID = faveChannel.getName();
                    mFaveList.add(showID);
                }
                initChannelList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
