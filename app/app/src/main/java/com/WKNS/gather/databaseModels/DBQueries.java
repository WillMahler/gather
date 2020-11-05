package com.WKNS.gather.databaseModels;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.WKNS.gather.databaseModels.Users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public static void registerUser(String userID, String firstName, String lastName){
        User user = new User(firstName, lastName);
        db.collection("users").document(userID)
                .set(user)
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

    /**
     *
     * @param userID
     * @param context
     * @param photoUri
     */
    public void setUserPhoto(String userID, Context context, URI photoUri){
        
    }
}
