package com.example.kostya.channeltask.activity.accelerometer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
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
import com.example.kostya.channeltask.fragment.accelerometer_fragments.AccelerometerDataFragment;
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

public class AccelerometerTaskActivity extends AppCompatActivity implements  AccelerometerSessionFragment.OnSessionClickListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Spinner mSensorDelaySpinner;
    private CountDownTimer mSensorUpdateTimer;
    private int mSensorDelayMS = 1000;
    private int mSessionId;
    private String mSessionStartDate = getSessionStartDate();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_task);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorDelaySpinner = (Spinner) findViewById(R.id.sensor_update_time_spinner);
        EditText serviceDurationInput = (EditText) findViewById(R.id.service_duration_edit_text);
        serviceDurationInput.setOnEditorActionListener(mOnEditorActionListener);

        mSessionId = PrefManager.getPrefManager().getLastSessionNumber(AccelerometerTaskActivity.this);
        spinnerSelectedItemClickListener();
        replaceFragment(new AccelerometerSessionFragment());
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelSensorUpdateTimer();
        unregisterSensor();
    }

    public void onClickStartAccSensor(View view) {
        Session data = new Session("session" + mSessionId + " " + mSessionStartDate);
        FirebaseHelper.uploadAccelerometerAllSessionsData(data);
        registerSensor();
    }


    public void onClickStopAccSensor(View view) {
        unregisterSensor();
        cancelSensorUpdateTimer();
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
                setSensorUpdateDuration(durationInSec);
                registerSensor();
            } else {
                unregisterSensor();
            }
            return false;
        }
    };

    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (mSensorUpdateTimer == null) {
                setSensorDelayTime(sensorEvent.values);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

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
        mSensorDelayMS = Integer.parseInt(msec) * 1000;
    }

    private void setSensorDelayTime(final float[] values) {
        mSensorUpdateTimer = new CountDownTimer(mSensorDelayMS, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                uploadAccelerometerDataToFirebase(values);
                mSensorUpdateTimer = null;
            }
        };
        mSensorUpdateTimer.start();
    }

    private void setSensorUpdateDuration(int duration) {
        CountDownTimer updateDuration = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                unregisterSensor();
                mSessionId++;
            }
        };
        updateDuration.start();
    }

    private void uploadAccelerometerDataToFirebase(float[] values) {
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyy HH:mm:ss");
        Date date = new Date();

        AccelerometerData data = new AccelerometerData(dateFormat.format(date), values[0], values[1], values[2]);
        FirebaseHelper.uploadAccelerometerData(data, mSessionId, mSessionStartDate);
    }

    private void cancelSensorUpdateTimer() {
        if (mSensorUpdateTimer != null) {
            mSensorUpdateTimer.cancel();
            mSessionId++;
            mSessionStartDate = getSessionStartDate();
            PrefManager.getPrefManager().setSessionNumber(mSessionId, AccelerometerTaskActivity.this);
        }
        mSensorUpdateTimer = null;
    }

    private String getSessionStartDate() {
        DateFormat sessionDate = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        Date date = new Date();
        return sessionDate.format(date);
    }

    private void registerSensor() {
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor
                , SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterSensor() {
        mSensorManager.unregisterListener(mSensorEventListener);
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

    @Override
    public void onSessionClick(String sessionName) {
        Intent intent = new Intent(AccelerometerTaskActivity.this, SessionActivity.class);
        intent.putExtra(SessionActivity.ARG_SESSION_NAME, sessionName);
        startActivity(intent);
    }
}
