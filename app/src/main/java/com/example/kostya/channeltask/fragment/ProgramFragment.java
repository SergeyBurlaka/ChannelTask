package com.example.kostya.channeltask.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.ChannelProgramHolder;
import com.example.kostya.channeltask.model.ChannelList;
import com.example.kostya.channeltask.model.ChannelProgram;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProgramFragment extends Fragment {
    public static final String ARG_CHANNEL_POSITION = "ARG_CHANNEL_POSITION";
    private List<String> mChannelName;
    private String mShowId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (mChannelName == null) {
                mChannelName = ChannelList.getsChannelList().getChannelName();
            }

            int currentPosition = getArguments().getInt(ARG_CHANNEL_POSITION, 0);
            mShowId = mChannelName.get(currentPosition);
            //when
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.channel_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initProgramList(recyclerView);
        return view;
    }

    private void initProgramList(RecyclerView recyclerView) {
        DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Program")
                .child("2016Jul08");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChannelProgram, ChannelProgramHolder>(ChannelProgram.class,
                R.layout.single_channel_program_info, ChannelProgramHolder.class, reference.orderByChild("showID").equalTo(mShowId)) {
            @Override
            protected void populateViewHolder(ChannelProgramHolder channelProgramHolder, ChannelProgram channelProgram, int position) {
                channelProgramHolder.setTvShowName(channelProgram.getTvShowName());
                channelProgramHolder.setDate(channelProgram.getDate());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static ProgramFragment newInstance(int position) {
        ProgramFragment fragment = new ProgramFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHANNEL_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
}
