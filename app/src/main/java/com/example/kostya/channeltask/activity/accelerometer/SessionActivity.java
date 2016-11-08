package com.example.kostya.channeltask.activity.accelerometer;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.adapter.ViewPagerAdapter;
import com.google.firebase.crash.FirebaseCrash;

public class SessionActivity extends AppCompatActivity {
    public static final String ARG_SESSION_NAME = "ARG_SESSION_NAME";
    private String mSessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

//        FirebaseCrash.report(new Exception("Non-fatal error. Session activity created"));

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            mSessionName = extra.getString(ARG_SESSION_NAME);
        }

        ViewPager sessionViewPager = (ViewPager)findViewById(R.id.session_view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mSessionName);
        sessionViewPager.setAdapter(adapter);
    }
}
