package com.example.kostya.channeltask.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.Channel;
import com.example.kostya.channeltask.model.ChannelList;
import com.example.kostya.channeltask.model.ChannelProgram;


public class ChannelProgramViewPagerFragment extends Fragment {
    private ViewPager mChannelProgramViewPager;

    public ChannelProgramViewPagerFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_program, container, false);
        mChannelProgramViewPager = (ViewPager) view.findViewById(R.id.channel_program_view_pager);

        mChannelProgramViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return ChannelList.getChannelSize();
            }

            @Override
            public Fragment getItem(int position) {
                return ProgramFragment.newInstance(position);
            }
        });
        return view;
    }
}
