package com.example.kostya.channeltask.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.Channel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChannelProgramViewPagerFragment extends Fragment {
    private ViewPager mChannelProgramViewPager;

    public ChannelProgramViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    List<String> mChannelName = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_program, container, false);
        mChannelProgramViewPager = (ViewPager) view.findViewById(R.id.channel_program_view_pager);
        getChannelList();


        return view;
    }

    private void getChannelList() {
        DatabaseReference reference = FirebaseHelper.getChannelReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot channelSnapShot : dataSnapshot.getChildren()) {
                    Channel channel = channelSnapShot.getValue(Channel.class);
                    String showID = channel.getId();
                    mChannelName.add(showID);
                }
                initViewPagerAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initViewPagerAdapter() {
        mChannelProgramViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return mChannelName.size();
            }

            @Override
            public Fragment getItem(int position) {
                return ProgramFragment.newInstance(mChannelName.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mChannelName.get(position);
            }
        });
    }

}
