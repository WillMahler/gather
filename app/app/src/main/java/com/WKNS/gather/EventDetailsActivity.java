package com.WKNS.gather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.WKNS.gather.databaseModels.Events.Attendee;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsPeople;
import com.WKNS.gather.ui.tabbedViewFragments.EventDetailsSummary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    public static final String TAG = EventDetailsActivity.class.getSimpleName();

    private Event mEventObj;
    private ArrayList<Attendee> mAccepted, mInvited, mDenied;
    private String mEventID;
    private Toolbar actionbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mAdapter;
    private FirebaseFirestore mDb;
    private DocumentReference mEventDoc;
    private CollectionReference mEventInvitedCollection;
    //Listeners for event attendees/invites to be updated. Used to update recycler views.
    private AttendeeRefreshListener attendeeRefreshListener;
    private InvitationRefreshListener invitationRefreshListener;
    private InvitationDeniedRefreshListener invitationDeniedRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //EventID
        mEventID = getIntent().getStringExtra("EVENT_ID");

        // mDb setup
        mDb = FirebaseFirestore.getInstance();

        // Doc refference setup
        mEventDoc = mDb.collection("events").document(mEventID);

        // Recyclerview Lists Setup
        mAccepted = new ArrayList<Attendee>();
        mInvited = new ArrayList<Attendee>();
        mDenied = new ArrayList<Attendee>();

        // Tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        // View pager
        mViewPager  = (ViewPager) findViewById(R.id.view_pager);

        // Adapter
        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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
        listenAttendees();
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
                        Log.d(TAG, "getEventSummary() - document does not exist");
                    }
                } else {
                    Log.d(TAG, "getEventSummary() - task was unsuccessful");
                }
            }});
    }
    //Queries the invited people.
    private void listenAttendees(){
        mEventInvitedCollection = mDb.collection("events").document(mEventID)
                .collection("attendees");

        //Listening for attendees
        mEventInvitedCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                /*
                Rebuilds all three lists anytime a change is made a lil inefficnet
                but the fastest way I found to update recyclerviews without keeping
                track of where the difference is in a recycler view.
                 */
                mInvited = new ArrayList<>();
                mAccepted = new ArrayList<>();
                mDenied = new ArrayList<>();

                for (QueryDocumentSnapshot doc : value) {
                    Attendee a = doc.toObject(Attendee.class);
                    a.setID(doc.getId()); //Store the id in the obj, (implict on firebase through the doc ID)

                    switch(a.getStatus()){
                        case 0:
                            mInvited.add(a);
                            break;
                        case 1:
                            mAccepted.add(a);
                            break;
                        case 2:
                            mDenied.add(a);
                    }
                }
                if(getAttendeeRefreshListener()!=null){
                    getAttendeeRefreshListener().onRefresh(mAccepted);
                }
                if(getInvitationRefreshListener()!=null){
                    getInvitationRefreshListener().onRefresh(mInvited);
                }
                if(getInvitationDeniedRefreshListener()!=null){
                    getInvitationDeniedRefreshListener().onRefresh(mDenied);
                }
            }
        });
    }

    /*TODO: If there is time, refactor this so that only one recycler view for a User is needed.
     * This would need the implementation of a Super-class for a user that has the basic info
     * of a user. I tried doing this but it required a lot of changes to every refference
     * of user type objects like Users, Attendees, Invites, which created problems with quering
     * them from firebase.
       -Nick*/
    //Interfaces for fragment to listen for updates in attendees
    public interface AttendeeRefreshListener{
        void onRefresh(ArrayList<Attendee> attendees);
    }

    public AttendeeRefreshListener getAttendeeRefreshListener() {
        return attendeeRefreshListener;
    }

    public void setAttendeeRefreshListener(AttendeeRefreshListener attendeeRefreshListener) {
        this.attendeeRefreshListener = attendeeRefreshListener;
    }

    //Interfaces for fragment to listen for updates in invited users
    public interface InvitationRefreshListener{
        void onRefresh(ArrayList<Attendee> attendees);
    }

    public InvitationRefreshListener getInvitationRefreshListener() {
        return invitationRefreshListener;
    }

    public void setInvitationRefreshListener(InvitationRefreshListener invitationRefreshListener) {
        this.invitationRefreshListener = invitationRefreshListener;
    }

    //Interfaces for fragment to listen for updates in invited users who denied
    public interface InvitationDeniedRefreshListener{
        void onRefresh(ArrayList<Attendee> attendees);
    }

    public InvitationDeniedRefreshListener getInvitationDeniedRefreshListener() {
        return invitationDeniedRefreshListener;
    }

    public void setInvitationDeniedRefreshListener(InvitationDeniedRefreshListener invitationDeniedRefreshListener) {
        this.invitationDeniedRefreshListener = invitationDeniedRefreshListener;
    }

    public Event getmEventObj(){ return mEventObj; };
    public ArrayList<Attendee> getAccepted(){ return mAccepted; }
    public ArrayList<Attendee> getInvited(){ return mInvited; }
    public ArrayList<Attendee> getDenied(){ return mDenied; }

}
