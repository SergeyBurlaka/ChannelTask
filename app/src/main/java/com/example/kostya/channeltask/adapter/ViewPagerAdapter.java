package com.example.kostya.channeltask.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kostya.channeltask.fragment.accelerometer_fragments.AccelerometerDataFragment;
import com.example.kostya.channeltask.fragment.accelerometer_fragments.AccelerometerGraphFragment;

/**
 * Created by kostya on 07.11.16.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private  String mSessionName;
    public ViewPagerAdapter(FragmentManager fm, String sessionName) {
        super(fm);
        mSessionName = sessionName;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AccelerometerDataFragment.newInstance(mSessionName);
            case 1:
                return AccelerometerGraphFragment.newInstance(mSessionName);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Session data";
            case 1:
                return "Graph";
            default:
                return null;
        }
    }
}