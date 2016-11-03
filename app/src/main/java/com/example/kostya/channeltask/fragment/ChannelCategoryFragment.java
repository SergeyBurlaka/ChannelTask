package com.example.kostya.channeltask.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.activity.SelectedCategoryActivity;
import com.example.kostya.channeltask.holder.CategoryHolder;
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.model.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChannelCategoryFragment extends Fragment {

    private static final String TAG = ChannelCategoryFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_channel_list, container, false);
        RecyclerView recyclerView = (RecyclerView)  view.findViewById(R.id.channel_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initCategoryList(recyclerView);
        return view;
    }

    private void initCategoryList(RecyclerView recyclerView) {
        final String[] categories = getResources().getStringArray(R.array.category_list);
        final DatabaseReference reference = FirebaseHelper.getCategoryReference();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, CategoryHolder>(Channel.class,
                R.layout.fragment_channel_item, CategoryHolder.class, reference) {
            @Override
            protected void populateViewHolder(CategoryHolder categoryHolder, Channel channel, final int position) {
                categoryHolder.setName(categories[position]);
                openSelectedCategory(categoryHolder, position);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void openSelectedCategory(CategoryHolder categoryHolder, final int position){
        categoryHolder.setOnCategoryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelectedCategoryActivity.class);
                intent.putExtra(SelectedCategoryActivity.KEY_CATEGORY_SELECTED_POSITION, position);
                startActivity(intent);
                if (DEBUG) Log.i(TAG, "onItemClickGeneral: position " + position);
            }
        });
    }
}
