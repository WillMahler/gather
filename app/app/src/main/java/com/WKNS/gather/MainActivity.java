package com.WKNS.gather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar actionBar;
    private BottomNavigationView navView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User userObject;
    private ArrayList<UserEvent> mUserEvents;
    //private CollectionReference userEventsCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = findViewById(R.id.actionbar);
        navView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setSupportActionBar(actionBar);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_search, R.id.navigation_calendar, R.id.navigation_notification, R.id.navigation_profile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // retrieving user data from database
        DocumentReference documentReference = db.collection("users").document(mAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null) {
                    userObject = new User(mAuth.getCurrentUser().getUid(), value.getString("email"), value.getString("firstName"), value.getString("lastName"), value.getString("profileImg"));
                }
            }
        });

        CollectionReference userEventsCollection = db.collection("users").document(mAuth.getUid())
                .collection("userEvents");
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(MainActivity.this, "Log Out Success.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Log Out Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listenUserEvents(){
        //TODO: BATCH the requests for events, limit it to 15 most recent events??


        userEventsCollection.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(mUserEvents == null) { mUserEvents = new ArrayList<UserEvent>(); }
                if (e != null) {
                    return;
                }
                int sizeMUserevents = mUserEvents.size();
                int addedDocuments = 0;
                for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                        //Log.d(TAG, "New city:" + change.getDocument().getData());
                    }
                    UserEvent retrieved = change.getDocument().toObject(UserEvent.class);
                    retrieved.eventID(change.getDocument().getReference().getId());

                    mUserEvents.add(retrieved);
                    addedDocuments++;
                    String source = querySnapshot.getMetadata().isFromCache() ?
                            "local cache" : "server";

                    Log.d("FETCH ", "Data fetched from " + source);
                }
            }
        });;

    }

    public User getUserObject() {
        return userObject;
    }
    public String getUserID(){return mAuth.getUid(); }
    public FirebaseFirestore getFireStoreDB() {return db; }
}
