package com.example.kostya.channeltask.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.Prefs.PrefManager;
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChannelListFragment extends Fragment {
    public static String ARG_CATEGORY_POSITION = "CATEGORY_POSITION";
    private List<String> mFaveList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private DatabaseReference mCategoryDBReference;
    private String mSelectedCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int position = getArguments().getInt(ARG_CATEGORY_POSITION);
            String[] categories = getResources().getStringArray(R.array.category_list);
            mSelectedCategory = categories[position];
            mCategoryDBReference = FirebaseHelper.getChannelReference();
        }
    }

    public static ChannelListFragment newInstance(int position) {
        ChannelListFragment fragment = new ChannelListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_POSITION, position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.channel_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checkFaveChannels();
        return view;
    }

    private void initChannelList() {
        final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, mSelectedCategory != null ?
                mCategoryDBReference.orderByChild(mSelectedCategory).equalTo(true)
                : mCategoryDBReference
        ) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {

                channelHolder.highlight(mFaveList.contains(channel.getId()));
                channelHolder.setName(channel.getName());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void checkFaveChannels() {
        String uniqueUser = PrefManager.getPrefManager().getUniqueUser(getContext());
        mFaveList = FirebaseHelper.getFaveChannelsList(uniqueUser);
        if (mCategoryDBReference == null)
            mCategoryDBReference = FirebaseHelper.getChannelReference();
        initChannelList();
    }
}
