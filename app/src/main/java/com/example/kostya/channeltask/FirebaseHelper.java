package com.example.kostya.channeltask;

import com.example.kostya.channeltask.model.acc_model.Session;
import com.example.kostya.channeltask.model.channel_model.Channel;
import com.example.kostya.channeltask.model.User;
import com.example.kostya.channeltask.model.acc_model.AccelerometerData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 03.11.16.
 */

public class FirebaseHelper {
    private static String sUniqueUserId;

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

    public static DatabaseReference getFaveReference() {
        return FIREBASE_FAVES_REFERENCE.child(sUniqueUserId);
    }

    public static Query orderByDateQuery(String sessionName) {
        return getDataFromSession(sessionName)
                .orderByChild("date");
    }

    public static Query orderBySessionDate() {
        return getDataFromAllAccelerometerSessions()
                .orderByChild("date")
                .startAt("session");
    }

    public static DatabaseReference getAccelerometerDataForUser(String sessionName) {

        return FIREBASE_ACCELEROMETER_REFERENCE
                .child(sUniqueUserId)
                .child(sessionName);
    }

    public static void uploadAccelerometerSessionData(Session data) {
        FIREBASE_ACCELEROMETER_ALL_SESSIONS
                .child(sUniqueUserId)
                .push()
                .setValue(data);
    }

    public static DatabaseReference getDataFromAllAccelerometerSessions() {
        return FIREBASE_ACCELEROMETER_ALL_SESSIONS
                .child(sUniqueUserId);
    }

    public static DatabaseReference getDataFromSession(String sessionName) {
        return FIREBASE_ACCELEROMETER_REFERENCE
                .child(sUniqueUserId)
                .child(sessionName);
    }

    public static void uploadAccelerometerData(AccelerometerData data, String sessionStartDate) {
        FIREBASE_ACCELEROMETER_REFERENCE
                .child(sUniqueUserId)
                .child("session" + " " + sessionStartDate)
                .push()
                .setValue(data);
    }

    public static void addToFave(String showId) {
        Channel channel = new Channel(showId);

        FIREBASE_FAVES_REFERENCE
                .child(sUniqueUserId)
                .child(showId)
                .setValue(channel);
    }

    public static void addUser(String uniqueUserId, User user) {
        FIREBASE_USER_REVERENCE
                .child(uniqueUserId)
                .setValue(user);
        sUniqueUserId = uniqueUserId;
    }

    public static void deleteFromFave(String channelName) {
        FIREBASE_FAVES_REFERENCE
                .child(sUniqueUserId)
                .child(channelName)
                .removeValue();
    }

    public static void deleteUser(String uniqueUserId) {
        FIREBASE_USER_REVERENCE
                .child(uniqueUserId)
                .removeValue();
    }

    public static List<String> getFaveChannelsList() {
        DatabaseReference reference = FIREBASE_FAVES_REFERENCE.child(sUniqueUserId);

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

}
