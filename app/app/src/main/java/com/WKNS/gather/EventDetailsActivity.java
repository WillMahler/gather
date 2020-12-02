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
import com.WKNS.gather.databaseModels.Events.Invitation;
import com.WKNS.gather.databaseModels.Users.UserEvent;
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
    private ArrayList<Attendee> mAttendees;
    private ArrayList<Invitation> mInvites, mInvitesDenied;   //Unaccapted and Denied Invites
    private String mEventID;
    private Toolbar actionbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mAdapter;
    private FirebaseFirestore mDb;
    private DocumentReference mEventDoc;
    private CollectionReference mEventAttendeesCollection;
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
        mAttendees = new ArrayList<Attendee>();
        mInvites = new ArrayList<Invitation>();
        mInvitesDenied = new ArrayList<Invitation>();

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
        listenEventUsers();
        listenInvited();
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
    private void listenInvited(){
        mEventInvitedCollection = mDb.collection("events").document(mEventID)
                .collection("invited");

        mEventInvitedCollection.whereEqualTo("didDenied", false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                /*Rebuilds the list every events are retrieved/ a change is made
                * Both are rebuilt together to save space, little inefficient
                * if a user accepts the invite*/
                mInvites = new ArrayList<>();
                mInvitesDenied = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Invitation newInvitation = doc.toObject(Invitation.class);
                    newInvitation.setID(doc.getId()); //Store the id in the obj, (implict on firebase through the doc ID)
                    if (newInvitation.isDidDenied()) {
                        mInvitesDenied.add(newInvitation);
                    } else {
                        mInvites.add(newInvitation);
                    }
                }
//                if(getAttendeeRefreshListener()!=null){
//                    getAttendeeRefreshListener().onRefresh(mAttendees);
//                }
            }
        });
    }

    private void listenEventUsers(){
        mEventAttendeesCollection = mDb.collection("events").document(mEventID)
                .collection("attendees");

        mEventAttendeesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                //Rebuilds the list every events are retrieved/ a change is made
                mAttendees = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Attendee newAttendee = doc.toObject(Attendee.class);
                    newAttendee.setID(doc.getId()); //Store the id in the obj, (implict on firebase through the doc ID)
                    mAttendees.add(newAttendee);
                }
                if(getAttendeeRefreshListener()!=null){
                    getAttendeeRefreshListener().onRefresh(mAttendees);
                }
            }
        });
    }

    /*TODO: Refactor this so that it doesn't resuse code like this, this was the only
    *  way I found to get the recyclerviews to update properly. -Nick*/
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
        void onRefresh(ArrayList<Invitation> attendees);
    }

    public InvitationRefreshListener getInvitationRefreshListener() {
        return invitationRefreshListener;
    }

    public void setInvitationRefreshListener(InvitationRefreshListener invitationRefreshListener) {
        this.invitationRefreshListener = invitationRefreshListener;
    }

    //Interfaces for fragment to listen for updates in invited users who denied
    public interface InvitationDeniedRefreshListener{
        void onRefresh(ArrayList<Invitation> attendees);
    }

    public InvitationDeniedRefreshListener getInvitationDeniedRefreshListener() {
        return invitationDeniedRefreshListener;
    }

    public void setInvitationDeniedRefreshListener(InvitationDeniedRefreshListener invitationDeniedRefreshListener) {
        this.invitationDeniedRefreshListener = invitationDeniedRefreshListener;
    }

    public Event getmEventObj(){ return mEventObj; };
    public ArrayList<Attendee> getAttendees(){ return mAttendees; }
    public ArrayList<Invitation> getInvited(){ return mInvites; }
    public ArrayList<Invitation> getDeclined(){ return mInvitesDenied; }

}
