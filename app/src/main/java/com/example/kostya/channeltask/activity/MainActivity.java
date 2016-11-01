package com.example.kostya.channeltask.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.fragment.ChannelFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity implements ChannelFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            replaceFragment();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                replaceFragment();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }

    public void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChannelFragment fragment = new ChannelFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onListFragmentInteraction() {

    }
}
