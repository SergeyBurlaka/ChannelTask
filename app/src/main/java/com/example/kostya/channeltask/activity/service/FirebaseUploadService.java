package com.example.kostya.channeltask.activity.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.activity.accelerometer.AccelerometerTaskActivity;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.example.kostya.channeltask.model.acc_model.Session;
import com.example.kostya.channeltask.prefs.PrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FirebaseUploadService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = FirebaseUploadService.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private CountDownTimer mSensorUpdateTimer;
    private int mSensorDelayMS = 1000;
    private String mSessionStartDate = getSessionStartDate();
    private IBinder mIBinder = new FirebaseUploadServiceBinder();

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
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service stopped");
        unregisterSensor();
        super.onDestroy();
    }

    //============================================= Interaction with Service=======================================
    public void startAccSensor() {
        boolean isRunning = PrefManager.getPrefManager().getIsSessionStarted(FirebaseUploadService.this);
        if (!isRunning) {
            addSessionToFirebase();
            setNotification();
            registerSensor();
        }
    }

    public void stopAccSensor() {
        unregisterSensor();
        cancelSensorUpdateTimer();
        stopForeground(true);
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
                stopForeground(true);
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
        PrefManager.getPrefManager().setIsSessionStarted(false, FirebaseUploadService.this);
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    public void setNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("Service is working")
                .setContentText("Uploading data");

        Intent intent = new Intent(this, AccelerometerTaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis() / 1000,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        startForeground(NOTIFICATION_ID, notification);
    }

}
