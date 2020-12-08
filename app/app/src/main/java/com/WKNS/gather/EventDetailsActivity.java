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
import com.WKNS.gather.databaseModels.Users.User;
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
    private CollectionReference mEventInvitedCollection;
    private ArrayList<Attendee> mListAccepted, mListInvited, mListDeclined;

    //Listeners for event attendees/invites to be updated. Used to update recycler views.
    private AttendeeRefreshListener attendeeRefreshListener;
    private InvitationRefreshListener invitationRefreshListener;
    private InvitationDeclinedRefreshListener invitationDeclinedRefreshListener;

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
                    Log.d(TAG, "listenAttendees(): Listen failed.", e);
                    return;
                }
                /*
                Rebuilds all three lists anytime a change is made a lil inefficnet
                but the fastest way I found to update recyclerviews without keeping
                track of where the difference is in a recycler view.
                 */
                mListInvited.clear();
                mListAccepted.clear();
                mListDeclined.clear();

                for (QueryDocumentSnapshot doc : value) {
                    Attendee a = doc.toObject(Attendee.class);
                    a.setID(doc.getId()); //Store the id in the obj, (implict on firebase through the doc ID)

                    switch (a.getStatus()) {
                        case 0:
                            mListInvited.add(a);
                            break;
                        case 1:
                            mListAccepted.add(a);
                            break;
                        case 2:
                            mListDeclined.add(a);
                    }
                }

//                if (getAttendeeRefreshListener()!=null) {
//                    getAttendeeRefreshListener().onRefresh(mListAccepted);
//                }
//                if (getInvitationRefreshListener()!=null) {
//                    getInvitationRefreshListener().onRefresh(mListInvited);
//                }
//                if (getInvitationDeclinedRefreshListener()!=null) {
//                    getInvitationDeclinedRefreshListener().onRefresh(mListDeclined);
//                }
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
    public interface AttendeeRefreshListener {
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
    public interface InvitationDeclinedRefreshListener {
        void onRefresh(ArrayList<Attendee> attendees);
    }

    public InvitationDeclinedRefreshListener getInvitationDeclinedRefreshListener() {
        return invitationDeclinedRefreshListener;
    }

    public void setInvitationDeclined(InvitationDeclinedRefreshListener invitationDeniedRefreshListener) {
        this.invitationDeclinedRefreshListener = invitationDeniedRefreshListener;
    }

    public Event getmEventObj(){ return mEventObj; }
    public String getmEventID() { return  mEventID; }
    public ArrayList<Attendee> getAccepted(){ return mListAccepted; }
    public ArrayList<Attendee> getInvited(){ return mListInvited; }
    public ArrayList<Attendee> getDenied(){ return mListDeclined; }

    public User getUserObject() { return userObject; }
}
