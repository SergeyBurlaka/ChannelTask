package com.example.kostya.channeltask.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.ChannelHolder;
import com.example.kostya.channeltask.model.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectedCategoryActivity extends AppCompatActivity {
    public static final String KEY_CATEGORY_SELECTED_POSITION = "KEY_CATEGORY_SELECTED_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_channel_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int selectedCategoryPosition = bundle.getInt(KEY_CATEGORY_SELECTED_POSITION);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.channel_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            initSelectedChannelCategory(recyclerView, selectedCategoryPosition);
        }
    }

    private void initSelectedChannelCategory(RecyclerView recyclerView, int selectedCategoryPosition) {
        String[] categories = getResources().getStringArray(R.array.category_list);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Category").child(categories[selectedCategoryPosition]);

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Channel, ChannelHolder>(Channel.class,
                R.layout.fragment_channel_item, ChannelHolder.class, reference) {
            @Override
            protected void populateViewHolder(ChannelHolder channelHolder, Channel channel, int position) {
                channelHolder.setName(channel.getName());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
