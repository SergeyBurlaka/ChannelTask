package com.example.kostya.channeltask.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kostya.channeltask.model.UserInformation;
import com.example.kostya.channeltask.service.LoadAllChannelNameService;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.fragment.ChannelCategoryFragment;
import com.example.kostya.channeltask.fragment.ChannelFragment;
import com.example.kostya.channeltask.fragment.ChannelProgramViewPagerFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String CHANNEL_LIST_TAG = "ChannelListFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        firebaseLogin();
        initServiceDownload();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                setUserInfo();
                replaceWithChannelListFragment();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.channel_list) {
            replaceWithChannelListFragment();
            // Handle the camera action
        } else if (id == R.id.category_list) {
            replaceWithChannelCategoryFragment();

        } else if (id == R.id.channel_program_list) {
            replaceWithSingleChannelProgramFragment();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void firebaseLogin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            replaceWithChannelListFragment();
            //signed in
        } else {
            //not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setProviders(AuthUI.GOOGLE_PROVIDER)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    private void replaceWithChannelListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(CHANNEL_LIST_TAG) == null) {
            ChannelFragment fragment = new ChannelFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, CHANNEL_LIST_TAG)
                    .commit();
        } else {
            ChannelFragment fragment = new ChannelFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, CHANNEL_LIST_TAG)
                    .commit();
        }
    }

    private void replaceWithChannelCategoryFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChannelCategoryFragment fragment = new ChannelCategoryFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, CHANNEL_LIST_TAG)
                .commit();
    }

    private void replaceWithSingleChannelProgramFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChannelProgramViewPagerFragment fragment = new ChannelProgramViewPagerFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, CHANNEL_LIST_TAG)
                .commit();
    }

    private void initServiceDownload() {
        Intent intent = new Intent(LoadAllChannelNameService.ACTION_START_LOAD);
        intent.setClass(this, LoadAllChannelNameService.class);
        startService(intent);
    }

    private void setUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = null, email = null;
            for (UserInfo profile : user.getProviderData()) {
                if (profile.getDisplayName() != null)
                    name = profile.getDisplayName();
                if (profile.getEmail() != null)
                    email = profile.getEmail();
                uploadUserInfoToFirebase(name, email);
            }
        }
    }

    private void uploadUserInfoToFirebase(String name, String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        UserInformation user = new UserInformation(email, name);
        String uniqueId = email.replaceAll("[^A-Za-z]", "");
        reference.child("users").child(uniqueId).setValue(user);
    }

}
