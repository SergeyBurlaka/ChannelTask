package com.example.kostya.channeltask.activity.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Spinner;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.activity.accelerometer.AccelerometerTaskActivity;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.example.kostya.channeltask.model.acc_model.Session;
import com.example.kostya.channeltask.prefs.PrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirebaseUploadService extends Service {
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private CountDownTimer mSensorUpdateTimer;
    private int mSensorDelayMS = 1000;
    private String mSessionStartDate = getSessionStartDate();
    private IBinder mIBinder = new FirebaseUploadServiceBinder();
    public static final String ACTION_START_UPLOAD = "ACTION_START_UPLOAD";
    public static  final String ACTION_STOP_UPLOAD = "ACTION_STOP_UPLOAD";

    public FirebaseUploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_START_UPLOAD:
                    registerSensor();
                    break;
                case ACTION_STOP_UPLOAD:
                    unregisterSensor();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //============================================= Interaction with Service=======================================
    public void startAccSensor() {
        addSessionToFirebase();
        registerSensor();
    }

    public void stopAccSensor() {
        unregisterSensor();
        cancelSensorUpdateTimer();
    }

    public void setSensorDelayMS(int ms) {
        mSensorDelayMS = ms;
    }

    public void setServiceUpdateDuration(int duration) {
        CountDownTimer updateDuration = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                unregisterSensor();
                stopSelf();
            }
        };
        updateDuration.start();
    }

    public class FirebaseUploadServiceBinder extends Binder {
        public FirebaseUploadService getService() {
            return FirebaseUploadService.this;
        }
    }

    //=============================================End of Interaction with Service=======================================


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


    private void addSessionToFirebase() {
        Session data = new Session("session" + " " + mSessionStartDate);
        FirebaseHelper.uploadAccelerometerSessionData(data);
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
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyy HH:mm:ss");
        Date date = new Date();

        AccelerometerData data = new AccelerometerData(dateFormat.format(date), values[0], values[1], values[2]);
        FirebaseHelper.uploadAccelerometerData(data, mSessionStartDate);
    }


    private void cancelSensorUpdateTimer() {
        if (mSensorUpdateTimer != null) {
            mSensorUpdateTimer.cancel();
            mSessionStartDate = getSessionStartDate();
        }
        mSensorUpdateTimer = null;
    }

    private void registerSensor() {
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor
                , SensorManager.SENSOR_DELAY_NORMAL);
    }

    private String getSessionStartDate() {
        DateFormat sessionDate = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        Date date = new Date();
        return sessionDate.format(date);
    }

    private void unregisterSensor() {
        mSensorManager.unregisterListener(mSensorEventListener);
    }

}
