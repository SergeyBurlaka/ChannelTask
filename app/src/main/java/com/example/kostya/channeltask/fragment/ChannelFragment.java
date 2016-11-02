package com.example.kostya.channeltask.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.Channel;
import com.example.kostya.channeltask.model.ChannelList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChannelFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.channel_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initChannelList(recyclerView);

        return view;
    }

    private void initChannelList(RecyclerView recyclerView) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Channels");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, reference) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {
                channelHolder.setName(channel.getName());
//                if (ChannelList.getChannelName() == null) {
//                    ChannelList list = ChannelList.getsChannelList();
//                    list.addChannel(channel.getName());
//                }
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
