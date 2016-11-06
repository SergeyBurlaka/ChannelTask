package com.example.kostya.channeltask.activity.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.fragment.accelerometer_fragments.AccelerometerGraphFragment;
import com.example.kostya.channeltask.fragment.accelerometer_fragments.ShowAccelerometerDataFragment;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.example.kostya.channeltask.prefs.PrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccelerometerTaskActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Spinner mSensorDelaySpinner;
    private int mSensorDelayMS = 2000;
    private CountDownTimer mSensorUpdateTimer;
    private int mSessionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_task);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorDelaySpinner = (Spinner) findViewById(R.id.sensor_update_time_spinner);

        spinnerSelectedItemClickListener();
        replaceFragment(new ShowAccelerometerDataFragment());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    public void onClickStartAccSensor(View view) {
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor
                , SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onClickStopAccSensor(View view) {
        mSensorManager.unregisterListener(mSensorEventListener);
        if (mSensorUpdateTimer != null)
            mSensorUpdateTimer.cancel();
        mSensorUpdateTimer = null;
        mSessionId++;

    }

    public void onClickShowAccelerometerDataFragment(View view) {
        replaceFragment(new ShowAccelerometerDataFragment());
    }

    public void onClickShowAccelerometerGraphFragment(View view) {
        replaceFragment(new AccelerometerGraphFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.accelerometer_fragment_container,
                        fragment)
                .commit();
    }

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
        String seconds = time.replaceAll("[^0-9]", "");
        mSensorDelayMS = Integer.parseInt(seconds) * 1000;
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

    private void uploadAccelerometerDataToFirebase(float[] values) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        AccelerometerData data = new AccelerometerData(dateFormat.format(date), values[0], values[1], values[2]);
        FirebaseHelper.uploadAccelerometerData(data, mSessionId);
        FirebaseHelper.uploadAccelerometerAllSessionsData(data);
        PrefManager.getPrefManager().setSessionNumber(mSessionId, AccelerometerTaskActivity.this);
    }
}
