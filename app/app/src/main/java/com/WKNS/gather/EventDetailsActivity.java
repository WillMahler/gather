package com.WKNS.gather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.WKNS.gather.databaseModels.Events.Attendee;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsPeople;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsSummary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    public static final String TAG = EventDetailsActivity.class.getSimpleName();

    private String mEventID;
    private Toolbar actionbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mAdapter;

    private FirebaseFirestore mDb;
    private DocumentReference mEventDoc;

    private User userObject;
    private Event mEventObj;
    private ArrayList<Attendee> mListAccepted, mListInvited, mListDeclined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        mEventID = getIntent().getStringExtra("EVENT_ID");

        mDb = FirebaseFirestore.getInstance();

        // Doc refference setup
        mEventDoc = mDb.collection("events").document(mEventID);

        mListAccepted = new ArrayList<>();
        mListInvited = new ArrayList<>();
        mListDeclined = new ArrayList<>();

        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.view_pager);
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // User Object
        Gson gson = new Gson();
        String userObjectString = getIntent().getStringExtra("USER_STR");
        userObject = gson.fromJson(userObjectString, User.class);

        mAdapter.addFrag(new EventDetailsSummary(mEventObj), getString(R.string.label_details));
        mAdapter.addFrag(new EventDetailsPeople(mEventObj), getString(R.string.label_people));

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("Event details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_round_arrow_back_ios_24));

        getEventSummary();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Queries event details from db.
    private void getEventSummary(){
        mEventDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mEventObj = document.toObject(Event.class);
                        mEventObj.setID(document.getId());
                        ((EventDetailsSummary) mAdapter.getItem(0)).setEventDetails(mEventObj);
                        ((EventDetailsSummary) mAdapter.getItem(0)).displayFAB(mEventObj);
                    } else {
                        Log.d(TAG, "getEventSummary() - document does not exist");
                    }
                } else {
                    Log.d(TAG, "getEventSummary() - task was unsuccessful");
                }
            }});
    }

    public String getmEventID() { return  mEventID; }
    public User getUserObject() { return userObject; }
}
