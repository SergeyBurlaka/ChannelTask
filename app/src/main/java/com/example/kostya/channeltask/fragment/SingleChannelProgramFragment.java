package com.example.kostya.channeltask.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.holder.ChannelProgramHolder;
import com.example.kostya.channeltask.model.Channel;
import com.example.kostya.channeltask.model.ChannelProgram;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SingleChannelProgramFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SingleChannelProgramFragment() {
        // Required empty public constructor
    }


    public static SingleChannelProgramFragment newInstance(String param1, String param2) {
        SingleChannelProgramFragment fragment = new SingleChannelProgramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        // Inflate the layout for this fragment
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
//                .child("tnt1467925200000")


//        reference.addValueEventListener(mValueEventListener);
        //TODO:Add normal references
        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChannelProgram, ChannelProgramHolder>(ChannelProgram.class,
                R.layout.single_channel_program_info, ChannelProgramHolder.class, reference.orderByChild("showID").equalTo("tnt")) {
            @Override
            protected void populateViewHolder(ChannelProgramHolder channelProgramHolder, ChannelProgram channelProgram, int position) {
                channelProgramHolder.setTvShowName(channelProgram.getTvShowName());
                channelProgramHolder.setShowId(channelProgram.getShowID());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        //works,but too much is going on in main thread and too much data

    }

//    private ValueEventListener mValueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            ChannelProgram program = dataSnapshot.getValue(ChannelProgram.class);
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
