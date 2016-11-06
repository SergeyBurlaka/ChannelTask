package com.example.kostya.channeltask.fragment.channel_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.channel_holder.ChannelProgramHolder;
import com.example.kostya.channeltask.model.channel_model.ChannelProgram;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;


public class ProgramFragment extends Fragment {
    public static final String ARG_CHANNEL_NAME = "ARG_CHANNEL_NAME";
    private String mShowId;
    private Button mAddToFaveButton;
    private static Toast mAddedToFaveToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShowId = getArguments().getString(ARG_CHANNEL_NAME);
        }
    }

    public static ProgramFragment newInstance(String showId) {
        if (mAddedToFaveToast != null)
            mAddedToFaveToast.cancel();
        ProgramFragment fragment = new ProgramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHANNEL_NAME, showId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAddToFaveButton = (Button) view.findViewById(R.id.add_to_fave);
        mAddToFaveButton.setVisibility(View.VISIBLE);

        initAddToFaveButton();
        initProgramList(recyclerView);
        return view;
    }

    private void initProgramList(RecyclerView recyclerView) {

        DatabaseReference reference = FirebaseHelper.getProgramReference();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChannelProgram, ChannelProgramHolder>(ChannelProgram.class,
                R.layout.fragment_program, ChannelProgramHolder.class, reference.orderByChild("showID").equalTo(mShowId)) {
            @Override
            protected void populateViewHolder(ChannelProgramHolder channelProgramHolder, ChannelProgram channelProgram, int position) {
                channelProgramHolder.setTvShowName(channelProgram.getTvShowName());
                channelProgramHolder.setDate(channelProgram.getDate());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void initAddToFaveButton() {
        mAddToFaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFaveChannel();
            }
        });
    }

    private void addFaveChannel() {
        FirebaseHelper.addToFave(mShowId);
        showToast();
    }

    private void showToast() {
        if (mAddedToFaveToast != null)
            mAddedToFaveToast.cancel();
        mAddedToFaveToast = Toast.makeText(getContext(), "Added to fave", Toast.LENGTH_SHORT);
        mAddedToFaveToast.show();
    }
}
