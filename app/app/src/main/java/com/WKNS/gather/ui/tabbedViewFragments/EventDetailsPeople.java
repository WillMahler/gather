package com.WKNS.gather.ui.tabbedViewFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.UserProfileActivity;
import com.WKNS.gather.databaseModels.Events.Attendee;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.recyclerViews.adapters.AttendeeRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventDetailsPeople extends Fragment {

    public static String TAG = EventDetailsPeople.class.getSimpleName();

    private ArrayList<Attendee> mInvited,  mAccepted, mDeclined;
    private Event mEventObj;
    private TextView mTextAttending, mTextInvited, mTextDeclined;
    private RecyclerView mRecyclerViewAttending, mRecyclerViewInvited, mRecyclerViewDeclined;
    private AttendeeRecyclerViewAdapter mAttendingAdapter, mInvitedAdapter, mDeclinedAdapter;
    private RecyclerView.LayoutManager mLayoutManagerAttending, mLayoutManagerInvited, mLayoutManagerDeclined;
    private CollectionReference mEventInvitedCollection;
    private FirebaseFirestore mDb;
    private EventDetailsActivity mEventDetailsActivity;
    private ListenerRegistration mListenerRegistration;

    public EventDetailsPeople(Event event) {
        mEventObj = event;
    }

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_details_people, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInvited = new ArrayList<>();
        mAccepted = new ArrayList<>();
        mDeclined = new ArrayList<>();

        mDb = FirebaseFirestore.getInstance();
        mEventDetailsActivity = (EventDetailsActivity) getActivity();

        mTextAttending = view.findViewById(R.id.textView_eventDetails_attending);
        mTextInvited = view.findViewById(R.id.textView_eventDetails_invited);
        mTextDeclined = view.findViewById(R.id.textView_eventDetails_declined);

        mTextAttending.setText(R.string.title_attending);
        mTextInvited.setText(R.string.title_invited);
        mTextDeclined.setText(R.string.title_declined);

        mRecyclerViewAttending = view.findViewById(R.id.recyclerView_eventDetails_attending);
        mRecyclerViewInvited = view.findViewById(R.id.recyclerView_eventDetails_invited);
        mRecyclerViewDeclined = view.findViewById(R.id.recyclerView_eventDetails_declined);

        mRecyclerViewAttending.setHasFixedSize(true);
        mRecyclerViewDeclined.setHasFixedSize(true);
        mRecyclerViewInvited.setHasFixedSize(true);

        mLayoutManagerAttending = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerDeclined = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerInvited = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewInvited.setLayoutManager(mLayoutManagerInvited);
        mRecyclerViewDeclined.setLayoutManager(mLayoutManagerDeclined);
        mRecyclerViewAttending.setLayoutManager(mLayoutManagerAttending);

        mAttendingAdapter = new AttendeeRecyclerViewAdapter(mAccepted);
        mDeclinedAdapter = new AttendeeRecyclerViewAdapter(mDeclined);
        mInvitedAdapter = new AttendeeRecyclerViewAdapter(mInvited);

        mRecyclerViewAttending.setAdapter(mAttendingAdapter);
        mRecyclerViewDeclined.setAdapter(mDeclinedAdapter);
        mRecyclerViewInvited.setAdapter(mInvitedAdapter);

        setUpClickListeners(mInvitedAdapter, mDeclinedAdapter, mAttendingAdapter);
    }

    private void setUpClickListeners(AttendeeRecyclerViewAdapter a1, AttendeeRecyclerViewAdapter a2, AttendeeRecyclerViewAdapter a3) {

        a1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                intent.putExtra("attendeeID", mInvited.get(position).getAttendeeID());
                startActivity(intent);
            }
        });

        a2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                intent.putExtra("attendeeID", mDeclined.get(position).getAttendeeID());
                startActivity(intent);
            }
        });

        a3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                intent.putExtra("attendeeID", mAccepted.get(position).getAttendeeID());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        attachListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachListener();
    }

    private void attachListener() {
        mEventInvitedCollection = mDb.collection("events").
                document(mEventDetailsActivity.getmEventID())
                .collection("attendees");

        mListenerRegistration = mEventInvitedCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "listenAttendees(): Listen failed.", e);
                    return;
                }

                mInvited.clear();
                mAccepted.clear();
                mDeclined.clear();

                for (QueryDocumentSnapshot doc : value) {
                    Attendee a = doc.toObject(Attendee.class);
                    a.setID(doc.getId());

                    switch (a.getStatus()) {
                        case 0:
                            mInvited.add(a);
                            break;
                        case 1:
                            mAccepted.add(a);
                            break;
                        case 2:
                            mDeclined.add(a);
                    }
                }

                mAttendingAdapter.notifyDataSetChanged();
                mInvitedAdapter.notifyDataSetChanged();
                mDeclinedAdapter.notifyDataSetChanged();
            }
        });
    }

    private void detachListener() {
        mListenerRegistration.remove();
    }
}
