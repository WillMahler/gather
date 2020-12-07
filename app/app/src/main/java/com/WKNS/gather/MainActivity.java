package com.WKNS.gather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar actionBar;
    private BottomNavigationView navView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private User userObject;
    private ArrayList<UserEvent> mUserEventsAccepted;
    private ArrayList<UserEvent> mUserEventsInvited;

    private DocumentReference userObjDoc;
    private CollectionReference userEventsCollection;

    //Listeners for fragments to be updated on userEvents
    private HomeFragmentRefreshListener homeFragRefreshListener;
    private NotificationFragmentRefreshListener notificationFragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = findViewById(R.id.actionbar);
        navView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setSupportActionBar(actionBar);

        mUserEventsAccepted = new ArrayList<>();
        mUserEventsInvited = new ArrayList<>();

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_create_event, R.id.navigation_notification, R.id.navigation_profile).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // click listener for create event button in bottom navigation bar
        findViewById(R.id.navigation_create_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String userObjectString = gson.toJson(userObject);

                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                intent.putExtra("userObjectString", userObjectString);
                startActivity(intent);
            }
        });

        // retrieving user data from database
        listenUser();
        listenUserEventsAccepted();
        listenUserEventsInvited();
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

    //This function listen to user profile information
    //TODO this was Saun's code that I moved around to match mine for better organization
    private void listenUser(){
        userObjDoc = db.collection("users").document(mAuth.getCurrentUser().getUid());
        userObjDoc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null) {
                    userObject = new User(mAuth.getCurrentUser().getUid(), value.getString("email"), value.getString("phoneNum"), value.getString("firstName"), value.getString("lastName"), value.getString("profileImg"), value.getString("bio"));
                    new DownloadImageTask().execute(userObject.getProfileImage());
                }
            }
        });
    }

    //This function listen to user events collection
    private void listenUserEventsInvited(){
        //TODO: BATCH the requests for events, limit it to 15 most recent events??
        userEventsCollection = db.collection("users").document(mAuth.getUid())
                .collection("userEvents");
        userEventsCollection.whereEqualTo("status", 0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "listenUserEventsInvited(): Listen Failed.", e);
                    return;
                }

                //Rebuilds the list every events are retrieved/ a change is made
                mUserEventsInvited.clear();
                for (QueryDocumentSnapshot doc : value) {
                    UserEvent newEvent = doc.toObject(UserEvent.class);
                    newEvent.setEventID(doc.getId()); //Store the id in the obj, (implict on firebase through the doc ID)
                    mUserEventsInvited.add(newEvent);
                }

                if (getNotificationRefreshListener()!=null) {
                    getNotificationRefreshListener().onRefresh(mUserEventsInvited);
                }
            }
        });
    }
    private void listenUserEventsAccepted(){
        //TODO: BATCH the requests for events, limit it to 15 most recent events??
        userEventsCollection = db.collection("users").document(mAuth.getUid())
                .collection("userEvents");
        userEventsCollection.whereEqualTo("status", 1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "listenUserEventsAccepted(): Listen failed.", e);
                    return;
                }

                //Rebuilds the list every events are retrieved/ a change is made
                mUserEventsAccepted.clear();
                for (QueryDocumentSnapshot doc : value) {
                    UserEvent newEvent = doc.toObject(UserEvent.class);
                    newEvent.setEventID(doc.getId()); //Store the id in the obj, (implict on firebase through the doc ID)
                    mUserEventsAccepted.add(newEvent);
                }

                if (getHomeRefreshListener()!=null) {
                    getHomeRefreshListener().onRefresh(mUserEventsAccepted);
                }
            }
        });
    }

    //Listener setup for home fragment to recieve updates for when userEvents are downloaded properly
    public interface HomeFragmentRefreshListener{
        void onRefresh(ArrayList<UserEvent> userEvents);
    }

    public HomeFragmentRefreshListener getHomeRefreshListener() {
        return homeFragRefreshListener;
    }

    public void setHomeFragmentRefreshListener(HomeFragmentRefreshListener homeRefreshListener) {
        this.homeFragRefreshListener = homeRefreshListener;
    }

    //Listener setup for notification fragment to recieve updates for when userEvents are downloaded properly
    public interface NotificationFragmentRefreshListener{
        void onRefresh(ArrayList<UserEvent> userEvents);
    }

    public NotificationFragmentRefreshListener getNotificationRefreshListener() {
        return notificationFragmentRefreshListener;
    }

    public void setNotificationFragmentRefreshListener(NotificationFragmentRefreshListener notificationRefreshListener) {
        this.notificationFragmentRefreshListener = notificationRefreshListener;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask() {
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            userObject.setProfileBitmap(result);
        }
    }

    public User getUserObject() { return userObject; }
    public ArrayList<UserEvent> getUserEvents(){ return mUserEventsAccepted; }
    public ArrayList<UserEvent> getUserEventsInvited(){ return mUserEventsInvited; }
    public String getUserID(){ return mAuth.getUid(); }
}
