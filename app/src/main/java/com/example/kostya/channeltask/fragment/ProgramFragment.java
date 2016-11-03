package com.example.kostya.channeltask.fragment;

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
import com.example.kostya.channeltask.Prefs.PrefManager;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.ChannelProgramHolder;
import com.example.kostya.channeltask.model.ChannelList;
import com.example.kostya.channeltask.model.ChannelProgram;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.List;


public class ProgramFragment extends Fragment {
    public static final String ARG_CHANNEL_POSITION = "ARG_CHANNEL_POSITION";
    private List<String> mChannelName;
    private String mShowId;
    private Button mAddToFaveButton;
    private static Toast mAddedToFaveToast;

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

    public static ProgramFragment newInstance(int position) {
        if (mAddedToFaveToast != null)
            mAddedToFaveToast.cancel();
        ProgramFragment fragment = new ProgramFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHANNEL_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.channel_recycler_view);
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
        String uniqueId = PrefManager.getPrefManager().getUniqueUser(getContext());

        FirebaseHelper.addToFave(uniqueId, mShowId);
        showToast();
    }

    private void showToast() {
        if (mAddedToFaveToast != null)
            mAddedToFaveToast.cancel();
        mAddedToFaveToast = Toast.makeText(getContext(), "Added to fave", Toast.LENGTH_SHORT);
        mAddedToFaveToast.show();
    }
}
