package com.WKNS.gather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsBudget;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsSummary;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsTasks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EventDetailsActivity extends AppCompatActivity {
    public static final String TAG = EventDetailsActivity.class.getSimpleName();

    private Event mEventObj;
    private String mEventID;
    private Toolbar actionbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mAdapter;
    private FirebaseFirestore mDb;
    private DocumentReference mEventDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //EventID
        mEventID = getIntent().getStringExtra("EXTRA_SESSION_ID");

        // mDb setup
        mDb = FirebaseFirestore.getInstance();

        // Doc refference setup
        mEventDoc = mDb.collection("events").document(mEventID);

        // Tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        // View pager
        mViewPager  = (ViewPager) findViewById(R.id.view_pager);

        // Adapter
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // 2 Tabs
        mAdapter.addFrag(new EventDetailsSummary(mEventObj), getString(R.string.tab_text_1));
        mAdapter.addFrag(new EventDetailsTasks(), getString(R.string.tab_text_2));
        mAdapter.addFrag(new EventDetailsBudget(), getString(R.string.tab_text_3));

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
                        ((EventDetailsSummary) mAdapter.getItem(0)).setEventDetails(mEventObj);
                    } else {

                    }
                } else {

                }
            }});
    }

    public Event getmEventObj(){ return mEventObj; };

}
