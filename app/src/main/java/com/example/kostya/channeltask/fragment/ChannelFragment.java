package com.example.kostya.channeltask.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.ChannelHolder;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ChannelFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;


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
        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, reference) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {
                channelHolder.setName(channel.getName());
            }
        };
        recyclerView.setAdapter(mFirebaseRecyclerAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }
}
