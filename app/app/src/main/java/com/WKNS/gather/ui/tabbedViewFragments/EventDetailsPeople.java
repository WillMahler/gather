package com.WKNS.gather.ui.tabbedViewFragments;

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
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Events.Attendee;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.databaseModels.Events.Invitation;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.adapters.AttendeeRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.adapters.InvitationRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.adapters.UserRecyclerViewAdapter;
import com.WKNS.gather.testData.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventDetailsPeople extends Fragment {

    private ArrayList<Invitation> mListInvited, mListDeclined;
    private ArrayList<Attendee> mListAttending;
    private Event mEventObj;
    private TextView mTextAttending, mTextInvited, mTextDeclined;
    private RecyclerView mRecyclerViewAttending, mRecyclerViewInvited, mRecyclerViewDeclined;
    private InvitationRecyclerViewAdapter mInvitedAdapter, mDeclinedAdapter;
    private AttendeeRecyclerViewAdapter mAttendingAdapter;
    private RecyclerView.LayoutManager mLayoutManagerAttending, mLayoutManagerInvited, mLayoutManagerDeclined;
    private CollectionReference mAttendeesCollection;

    public EventDetailsPeople(Event event) {
        mEventObj = event;
    }

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mListAttending = ((EventDetailsActivity)getActivity()).getAttendees();
        mListInvited = ((EventDetailsActivity)getActivity()).getInvited();
        mListDeclined = ((EventDetailsActivity)getActivity()).getDeclined();

        ((EventDetailsActivity)getActivity()).setAttendeeRefreshListener(new EventDetailsActivity.AttendeeRefreshListener() {
            @Override
            public void onRefresh(ArrayList<Attendee> attendees) {
                mListAttending.clear();
                mListAttending.addAll(attendees);
                mAttendingAdapter.notifyDataSetChanged();
            }
        });

        ((EventDetailsActivity)getActivity()).setInvitationRefreshListener(new EventDetailsActivity.InvitationRefreshListener() {
            @Override
            public void onRefresh(ArrayList<Invitation> invites) {
                mListInvited.clear();
                mListInvited.addAll(invites);
                mAttendingAdapter.notifyDataSetChanged();
            }
        });

        ((EventDetailsActivity)getActivity()).setInvitationDeniedRefreshListener(new EventDetailsActivity.InvitationDeniedRefreshListener() {
            @Override
            public void onRefresh(ArrayList<Invitation> invites) {
                mListDeclined.clear();
                mListDeclined.addAll(invites);
                mAttendingAdapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_details_people, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        mAttendingAdapter = new AttendeeRecyclerViewAdapter(mListAttending);
        mDeclinedAdapter = new InvitationRecyclerViewAdapter(mListDeclined);
        mInvitedAdapter = new InvitationRecyclerViewAdapter(mListInvited);

        mRecyclerViewAttending.setAdapter(mAttendingAdapter);
        mRecyclerViewDeclined.setAdapter(mDeclinedAdapter);
        mRecyclerViewInvited.setAdapter(mInvitedAdapter);
    }

}
