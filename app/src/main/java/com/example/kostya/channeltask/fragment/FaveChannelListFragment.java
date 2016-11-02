package com.example.kostya.channeltask.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.model.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by kostya on 02.11.16.
 */

public class FaveChannelListFragment extends Fragment {
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView mRecyclerView;
    private HashMap<Integer, String> mFaveChannelList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.channel_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFaveChannelList = new HashMap<>();

        initFaveList(mRecyclerView);
        swipeToDelete();
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

    private void initFaveList(RecyclerView mRecyclerView) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("faves");

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, reference) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {
                channelHolder.setName(channel.getName());
                mFaveChannelList.put(position, channel.getName());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void swipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView mRecyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder);
            }
        };
        mItemTouchHelper = new ItemTouchHelper(simpleCallback);
    }

    private void deleteItem(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        String name = mFaveChannelList.get(position);
        FirebaseDatabase.getInstance()
                .getReference()
                .child("faves")
                .child(name)
                .removeValue();

        initFaveList(mRecyclerView);
    }
}
