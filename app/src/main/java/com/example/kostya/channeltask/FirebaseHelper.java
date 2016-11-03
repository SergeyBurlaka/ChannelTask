package com.example.kostya.channeltask;

import com.example.kostya.channeltask.model.Channel;
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

    public static DatabaseReference getProgramReference() {
        return FIREBASE_PROGRAM_REFERENCE;
    }

    public static DatabaseReference getCategoryReference() {
        return FIREBASE_CATEGORY_REFERENCE;
    }

    public static DatabaseReference getChannelReference() {
        return FIREBASE_CHANNEL_REFERENCE;
    }


    public static void addToFave(String uniqueUserId, String showId) {
        Channel channel = new Channel(showId);

        FIREBASE_FAVES_REFERENCE
                .child(uniqueUserId)
                .child(showId)
                .setValue(channel);
    }

    public static List<String> getFaveChannelsList(String uniqueUserId) {
        DatabaseReference reference = FIREBASE_FAVES_REFERENCE.child(uniqueUserId);

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

    public static void deleteFromFave(String channelName, String uniqueUserId) {
        FIREBASE_FAVES_REFERENCE
                .child(uniqueUserId)
                .child(channelName)
                .removeValue();
    }
}
