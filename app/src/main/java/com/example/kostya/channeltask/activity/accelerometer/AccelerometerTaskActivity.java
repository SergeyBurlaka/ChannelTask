package com.example.kostya.channeltask.activity.accelerometer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.activity.ChooserActivity;
import com.example.kostya.channeltask.activity.service.FirebaseUploadService;
import com.example.kostya.channeltask.fragment.accelerometer_fragments.AccelerometerSessionFragment;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.example.kostya.channeltask.model.acc_model.Session;
import com.example.kostya.channeltask.prefs.PrefManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccelerometerTaskActivity extends AppCompatActivity implements AccelerometerSessionFragment.OnSessionClickListener {
    private Spinner mSensorDelaySpinner;
    private FirebaseUploadService mFirebaseUploadService;
    private boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_task);

        mSensorDelaySpinner = (Spinner) findViewById(R.id.sensor_update_time_spinner);
        EditText serviceDurationInput = (EditText) findViewById(R.id.service_duration_edit_text);
        serviceDurationInput.setOnEditorActionListener(mOnEditorActionListener);

//        startService();
        onBindService();

        spinnerSelectedItemClickListener();
        replaceFragment(new AccelerometerSessionFragment());

    }

    public void onClickStartAccSensor(View view) {
        startService(FirebaseUploadService.ACTION_START_UPLOAD);
        mFirebaseUploadService.startAccSensor();
    }

    public void onClickStopAccSensor(View view) {
        stopService();
        mFirebaseUploadService.stopAccSensor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSessionClick(String sessionName) {
        Intent intent = new Intent(AccelerometerTaskActivity.this, SessionActivity.class);
        intent.putExtra(SessionActivity.ARG_SESSION_NAME, sessionName);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        unBindService();
        super.onDestroy();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mFirebaseUploadService = ((FirebaseUploadService.FirebaseUploadServiceBinder) service).getService();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void onBindService() {
        Intent intent = new Intent(AccelerometerTaskActivity.this, FirebaseUploadService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void unBindService() {
        if (mIsBound) {
            unbindService(mServiceConnection);
            mIsBound = false;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.accelerometer_fragment_container,
                        fragment)
                .commit();
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (!textView.getText().toString().equals("")) {
                int durationInSec = Integer.parseInt(textView.getText().toString()) * 60000;
                mFirebaseUploadService.setServiceUpdateDuration(durationInSec);

//                setServiceUpdateDuration(durationInSec);
//                addSessionToFirebase();
//                registerSensor();
            } else {
//                unregisterSensor();
            }
            return false;
        }
    };

    private void spinnerSelectedItemClickListener() {
        mSensorDelaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedDuration = mSensorDelaySpinner.getSelectedItem().toString();
                updateSensorDuration(selectedDuration);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateSensorDuration(String time) {
        String msec = time.replaceAll("[^0-9]", "");
        int secs = Integer.parseInt(msec) * 1000;
        if (mFirebaseUploadService != null)
            mFirebaseUploadService.setSensorDelayMS(secs);
    }

//    private void setServiceUpdateDuration(int duration) {
//        CountDownTimer updateDuration = new CountDownTimer(duration, 1000) {
//            @Override
//            public void onTick(long l) {
//            }
//
//            @Override
//            public void onFinish() {
////                unregisterSensor();
//                stopService();
//            }
//        };
//        updateDuration.start();
//    }

    private void startService(String action) {

        Intent serviceIntent = new Intent(action);
        serviceIntent.setClass(AccelerometerTaskActivity.this, FirebaseUploadService.class);
        startService(serviceIntent);
    }

    private void stopService() {
        Intent intent = new Intent(AccelerometerTaskActivity.this, FirebaseUploadService.class);
        stopService(intent);
    }


    private void logout() {
        FirebaseHelper
                .deleteUser(PrefManager.getPrefManager()
                        .getUniqueUser(AccelerometerTaskActivity.this));

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(AccelerometerTaskActivity.this,
                                    ChooserActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
