package com.WKNS.gather.databaseModels;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.databaseModels.Events.EventPrivate;
import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.net.URI;

/**
 * A class used to query the database
 */
public class DBQueries {
    private static FirebaseFirestore db;

    public DBQueries() {}

    /**
     * Call this function after mAuth is authenticated to pass a db reference.
     * @param dbInit A copy of a db reference from FirebaseFirestore.getInstance();
     */
    public static void initDBQueries(FirebaseFirestore dbInit){
        db = dbInit;
    }

    public static void registerUser(User user){
        DocumentReference userDoc = db.collection("users").document(user.getUserID());

        userDoc.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    public void createEvent(Event event,  EventPrivate eventPrivate, UserEvent userEvent){
        DocumentReference eventDoc = db.collection("events").document();
        event.setID(eventDoc.getId());
        eventDoc.set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


    }

}
