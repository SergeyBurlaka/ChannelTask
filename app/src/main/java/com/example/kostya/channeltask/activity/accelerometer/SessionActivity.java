package com.example.kostya.channeltask.activity.accelerometer;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.adapter.ViewPagerAdapter;

public class SessionActivity extends AppCompatActivity {
    public static final String ARG_SESSION_NAME = "ARG_SESSION_NAME";
    private String mSessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            mSessionName = extra.getString(ARG_SESSION_NAME);
        }

        ViewPager sessionViewPager = (ViewPager)findViewById(R.id.session_view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mSessionName);
        sessionViewPager.setAdapter(adapter);
    }
}
