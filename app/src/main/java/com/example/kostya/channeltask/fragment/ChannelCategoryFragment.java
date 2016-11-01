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
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.model.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChannelCategoryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_channel_list, container, false);
        RecyclerView recyclerView = (RecyclerView)  view.findViewById(R.id.channel_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initCategoryList(recyclerView);
        return view;
    }

    private void initCategoryList(RecyclerView recyclerView) {
        final String[] categories = getResources().getStringArray(R.array.category_list);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Category");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, reference) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {
                channelHolder.setName(categories[position]);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


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
        void onFragmentInteraction(Uri uri);
    }
}
