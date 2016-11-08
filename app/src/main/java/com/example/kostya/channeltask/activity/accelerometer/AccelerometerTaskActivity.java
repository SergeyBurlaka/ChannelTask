package com.example.kostya.channeltask.activity.accelerometer;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import com.example.kostya.channeltask.prefs.PrefManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AccelerometerTaskActivity extends AppCompatActivity implements AccelerometerSessionFragment.OnAccelerometerSessionFragmentInteractions {
    private Spinner mSensorDelaySpinner;
    private FirebaseUploadService mFirebaseUploadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_task);

        mSensorDelaySpinner = (Spinner) findViewById(R.id.sensor_update_time_spinner);
        EditText serviceDurationInput = (EditText) findViewById(R.id.service_duration_edit_text);
        serviceDurationInput.setOnEditorActionListener(mOnEditorActionListener);

        onBindService();
        spinnerSelectedItemClickListener();
        replaceFragment(new AccelerometerSessionFragment());

    }

    public void onClickStartAccSensor(View view) {
        startService();
        mFirebaseUploadService.startAccSensor();
        PrefManager.getPrefManager().setIsSessionStarted(true, AccelerometerTaskActivity.this);
    }

    public void onClickStopAccSensor(View view) {
        mFirebaseUploadService.stopAccSensor();
        setIsRunning(false);

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
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mFirebaseUploadService = ((FirebaseUploadService.FirebaseUploadServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void onBindService() {
        Intent intent = new Intent(AccelerometerTaskActivity.this, FirebaseUploadService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
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

                mFirebaseUploadService.stopAccSensor();
                mFirebaseUploadService.setServiceUpdateDuration(durationInSec);
                mFirebaseUploadService.startAccSensor();
            } else {
                mFirebaseUploadService.stopAccSensor();
            }
            return false;
        }
    };

    private void setIsRunning(boolean isRunning) {
        PrefManager.getPrefManager().setIsSessionStarted(isRunning, AccelerometerTaskActivity.this);
    }

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

    private void startService() {
        startService(new Intent(AccelerometerTaskActivity.this,
                FirebaseUploadService.class));
    }


    private void logout() {
        boolean isRunning = PrefManager.getPrefManager().getIsSessionStarted(AccelerometerTaskActivity.this);

        if (!isRunning) {
            mFirebaseUploadService.stopAccSensor();
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
        } else {
            showAlertDialog();

        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccelerometerTaskActivity.this);
        builder
                .setTitle("Service will be stopped")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                PrefManager.getPrefManager().setIsSessionStarted(false, AccelerometerTaskActivity.this);
                                logout();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
