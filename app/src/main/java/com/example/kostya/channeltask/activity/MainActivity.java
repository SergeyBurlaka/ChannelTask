package com.example.kostya.channeltask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.Prefs.PrefManager;
import com.example.kostya.channeltask.fragment.ActionChooserFragment;
import com.example.kostya.channeltask.fragment.FaveChannelListFragment;
import com.example.kostya.channeltask.model.User;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.fragment.ChannelCategoryFragment;
import com.example.kostya.channeltask.fragment.ChannelListFragment;
import com.example.kostya.channeltask.fragment.ChannelProgramViewPagerFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActionChooserFragment.OnChooserFragmentInteractionListener
        , ChannelCategoryFragment.OnChannelCategoryItemClick {
    public static final String CHANNEL_LIST_TAG = "ChannelListFragment";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        mActionBarDrawerToggle.syncState();

        hideNavigationDrawer();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceWithFragment(new ChannelListFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.channel_list) {
            replaceWithFragment(new ChannelListFragment());
        } else if (id == R.id.category_list) {
            replaceWithFragment(new ChannelCategoryFragment());

        } else if (id == R.id.channel_program_list) {
            replaceWithFragment(new ChannelProgramViewPagerFragment());
        } else if (id == R.id.fave_channel_list) {
            replaceWithFragment(new FaveChannelListFragment());
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onChannelProgramClick() {
        hideNavigationDrawer();
        replaceWithFragment(new ChannelListFragment());
    }

    private void replaceWithFragment(Fragment fragment) {
        showNavigationDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, CHANNEL_LIST_TAG)
                .commit();
    }

    private void logout() {

        FirebaseHelper
                .deleteUser(PrefManager.getPrefManager()
                        .getUniqueUser(MainActivity.this));

//        DatabaseReference reference = FirebaseDatabase.getInstance()
//                .getReference();
//        reference.child("users")
//                .child(PrefManager.getPrefManager().getUniqueUser(this))
//                .removeValue();


        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            hideNavigationDrawer();
                            Intent intent = new Intent(MainActivity.this, ChooserActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void hideNavigationDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    private void showNavigationDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    }


    @Override
    public void onCategoryClick(int position) {
        ChannelListFragment fragment = ChannelListFragment.newInstance(position);
        replaceWithFragment(fragment);

    }
}
