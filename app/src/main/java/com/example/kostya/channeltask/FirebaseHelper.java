package com.example.kostya.channeltask;

import android.content.Context;

import com.example.kostya.channeltask.model.channel_model.Channel;
import com.example.kostya.channeltask.model.User;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 03.11.16.
 */

public class FirebaseHelper {
    private static String mUniqueUserId;

    private static final DatabaseReference FIREBASE_REFERENCE = FirebaseDatabase.getInstance().getReference();

    private static final DatabaseReference FIREBASE_CHANNEL_REFERENCE =
            FIREBASE_REFERENCE
                    .child("Channels");

    private static final DatabaseReference FIREBASE_CATEGORY_REFERENCE =
            FIREBASE_REFERENCE
                    .child("Category");

    private static final DatabaseReference FIREBASE_PROGRAM_REFERENCE =
            FIREBASE_REFERENCE
                    .child("Program")
                    .child("2016Jul08");

    private static final DatabaseReference FIREBASE_FAVES_REFERENCE =
            FIREBASE_REFERENCE
                    .child("faves");

    private static final DatabaseReference FIREBASE_USER_REVERENCE =
            FIREBASE_REFERENCE
                    .child("users");

    private static final DatabaseReference FIREBASE_ACCELEROMETER_REFERENCE =
            FIREBASE_REFERENCE
                    .child("Accelerometer");

    private static final DatabaseReference FIREBASE_ACCELEROMETER_ALL_SESSIONS =
            FIREBASE_REFERENCE
            .child("Accelerometer")
            .child("AllSessions");


    public static DatabaseReference getProgramReference() {
        return FIREBASE_PROGRAM_REFERENCE;
    }

    public static DatabaseReference getCategoryReference() {
        return FIREBASE_CATEGORY_REFERENCE;
    }

    public static DatabaseReference getChannelReference() {
        return FIREBASE_CHANNEL_REFERENCE;
    }

    public static DatabaseReference getUserReference(Context context) {
        return FIREBASE_USER_REVERENCE;
    }

    public static DatabaseReference getFaveReference(String uniqueUserId) {
        return FIREBASE_FAVES_REFERENCE.child(uniqueUserId);
    }

    public static DatabaseReference getAccelerometerReference() {
        return FIREBASE_ACCELEROMETER_REFERENCE;
    }

    public static DatabaseReference getAccelerometerAllSessions() {
        return FIREBASE_ACCELEROMETER_ALL_SESSIONS;
    }

    public static DatabaseReference getAccelerometerDataForUser(int sessionId) {

        return FIREBASE_ACCELEROMETER_REFERENCE
                .child(mUniqueUserId)
                .child("session" + sessionId);
    }

    public static void uploadAccelerometerAllSessionsData(AccelerometerData data) {
        FIREBASE_ACCELEROMETER_ALL_SESSIONS
                .child(mUniqueUserId)
                .push()
                .setValue(data);
    }

    public static DatabaseReference getDataFromAllAccelerometerSessions() {
        return FIREBASE_ACCELEROMETER_ALL_SESSIONS
                .child(mUniqueUserId);
    }

    public static void uploadAccelerometerData(AccelerometerData data, int sessionId) {
        FIREBASE_ACCELEROMETER_REFERENCE
                .child(mUniqueUserId)
                .child("session" + sessionId)
                .push()
                .setValue(data);
    }

    public static void addToFave(String showId) {
        Channel channel = new Channel(showId);

        FIREBASE_FAVES_REFERENCE
                .child(mUniqueUserId)
                .child(showId)
                .setValue(channel);
    }

    public static List<String> getFaveChannelsList() {
        DatabaseReference reference = FIREBASE_FAVES_REFERENCE.child(mUniqueUserId);

        final List<String> faveList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Channel faveChannel = data.getValue(Channel.class);
                    String showID = faveChannel.getName();
                    faveList.add(showID);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return faveList;
    }

    public static void deleteFromFave(String channelName) {
        FIREBASE_FAVES_REFERENCE
                .child(mUniqueUserId)
                .child(channelName)
                .removeValue();
    }

    public static void addUser(String uniqueUserId, User user) {
        FIREBASE_USER_REVERENCE
                .child(uniqueUserId)
                .setValue(user);
    }

    public static void deleteUser(String uniqueUserId) {
        FIREBASE_USER_REVERENCE
                .child(uniqueUserId)
                .removeValue();
    }
}
