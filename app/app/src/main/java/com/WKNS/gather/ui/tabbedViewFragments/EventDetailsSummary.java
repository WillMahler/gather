package com.WKNS.gather.ui.tabbedViewFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Events.Event;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailsSummary extends Fragment {
    private Event mEventObj;
    private ImageView mDisplayPic;
    private TextView mTitle, mDate, mLocation, mHost, mDescription;

    public EventDetailsSummary(Event event){
        mEventObj = event;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_details_summary, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDisplayPic = view.findViewById(R.id.imageView_eventDetails_displayPic);
        mTitle = view.findViewById(R.id.textView_eventDetails_title);
        mDate = view.findViewById(R.id.textView_eventDetails_date);
        mLocation = view.findViewById(R.id.textView_eventDetails_location);
        mHost = view.findViewById(R.id.textView_eventDetails_host);
        mDescription = view.findViewById(R.id.textView_eventDetails_description);

        mDisplayPic.setImageResource(R.drawable.ic_baseline_video_library_24);
        super.onViewCreated(view, savedInstanceState);

        if(mEventObj != null) setEventDetails(mEventObj);
    }

    public void setEventDetails(Event event){
        mEventObj = event;
        mTitle.setText(event.getTitle());
        mDate.setText(event.getDate().toString()); //TODO: Maybe have this not be toString()?
        mLocation.setText("123 Placeholder Street."); //TODO: Hard code these as string in Event and UserEvent or use api
        mHost.setText(event.getOwnerFirstName() + " " + event.getOwnerLastName());
        mDescription.setText(event.getDescription());
    }
}
