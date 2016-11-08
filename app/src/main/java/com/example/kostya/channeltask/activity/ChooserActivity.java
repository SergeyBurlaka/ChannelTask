package com.example.kostya.channeltask.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.activity.accelerometer.AccelerometerTaskActivity;
import com.example.kostya.channeltask.activity.channels.ChannelActivity;
import com.example.kostya.channeltask.prefs.PrefManager;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class ChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        firebaseLogin();
    }

    private void firebaseLogin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            initUserInfo();
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
                initUserInfo();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }

    public void onClickChannelTask(View view) {
        openChannelTaskActivity();
    }

    public void onClickAccelerometerTask(View view) {
        openAccelerometerActivity();
    }

    private void openChannelTaskActivity() {
        Intent intent = new Intent(ChooserActivity.this, ChannelActivity.class);
        startActivity(intent);
    }

    private void openAccelerometerActivity() {
        Intent intent = new Intent(ChooserActivity.this, AccelerometerTaskActivity.class);
        startActivity(intent);
    }

    private void initUserInfo() {
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
        User user = new User(email, name);
        String uniqueId = email.replaceAll("[^A-Za-z]", "");
        PrefManager.getPrefManager().setUniqueUser(uniqueId, this);

        FirebaseHelper.addUser(uniqueId, user);
    }
}
