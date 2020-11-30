package com.WKNS.gather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.ui.home.HomeFragment;
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
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar actionBar;
    private BottomNavigationView navView;

    //Firebase setup
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //User Information
    private User userObject;
    private ArrayList<UserEvent> mUserEvents;

    //Firebase db references
    private DocumentReference userObjDoc;
    private CollectionReference userEventsCollection;

    //Listeners for fragments to be updated on userEvents
    private HomeFragmentRefreshListener homeFragRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = findViewById(R.id.actionbar);
        navView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setSupportActionBar(actionBar);

        mUserEvents = new ArrayList<UserEvent>();
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_search, R.id.navigation_create_event, R.id.navigation_notification, R.id.navigation_profile).build();
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
        listenUserEvents();
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
    private void listenUserEvents(){
        //TODO: BATCH the requests for events, limit it to 15 most recent events??
        userEventsCollection = db.collection("users").document(mAuth.getUid())
                .collection("userEvents");
        userEventsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                //Rebuilds the list every events are retrieved/ a change is made
                mUserEvents = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    UserEvent newEvent = doc.toObject(UserEvent.class);
                    newEvent.eventID(doc.getId()); //Store the id in the obj, (implict on firebase through the doc ID)
                    mUserEvents.add(newEvent);
                }
                if(getFragmentRefreshListener()!=null){
                    getFragmentRefreshListener().onRefresh(mUserEvents);
                }
            }
        });
    }

    //Listener setup for home fragment to recieve updates for when userEvents are downloaded properly
    public interface HomeFragmentRefreshListener{
        void onRefresh(ArrayList<UserEvent> userEvents);
    }

    public HomeFragmentRefreshListener getFragmentRefreshListener() {
        return homeFragRefreshListener;
    }

    public void setHomeFragmentRefreshListener(HomeFragmentRefreshListener fragmentRefreshListener) {
        this.homeFragRefreshListener = fragmentRefreshListener;
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
    public ArrayList<UserEvent> getmUserEvents(){ return mUserEvents; }
    public String getUserID(){ return mAuth.getUid(); }
}
