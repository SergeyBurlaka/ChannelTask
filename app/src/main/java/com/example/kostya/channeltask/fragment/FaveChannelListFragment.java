package com.example.kostya.channeltask.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.prefs.PrefManager;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.model.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by kostya on 02.11.16.
 */

public class FaveChannelListFragment extends Fragment {
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView mRecyclerView;
    private HashMap<Integer, String> mFaveChannelMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFaveChannelMap = new HashMap<>();

        initFaveList();
        initSwipeToDelete();
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

    private void initFaveList() {

        String uniqueUserId = PrefManager.getPrefManager().getUniqueUser(getContext());
        DatabaseReference reference = FirebaseHelper
                .getFaveReference(uniqueUserId);

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, reference) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {
                channelHolder.setName(channel.getName());
                mFaveChannelMap.put(position, channel.getName());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void initSwipeToDelete() {
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
        String name = mFaveChannelMap.get(position);
        String uniqueId = PrefManager.getPrefManager().getUniqueUser(getContext());
        FirebaseHelper.deleteFromFave(name, uniqueId);

        initFaveList();
    }
}
