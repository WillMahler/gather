package com.WKNS.gather.ui.tabbedViewFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.helperMethods.DownloadImageTask;

public class EventDetailsSummary extends Fragment {

    public static String TAG = EventDetailsSummary.class.getSimpleName();

    private Event mEventObj;
    private ImageView mDisplayPic;
    private TextView mTitle, mDate, mLocation, mHost, mDescription;

    public EventDetailsSummary(Event event) {
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

        super.onViewCreated(view, savedInstanceState);

        setEventDetails(mEventObj);
    }

    public void setEventDetails(Event event) {
        if (event != null) {
            mEventObj = event;

            String photoURL = event.getPhotoURL();

            if (photoURL == null || photoURL.isEmpty()) {
                mDisplayPic.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
            } else {
                new DownloadImageTask(mDisplayPic).execute(photoURL);
            }

            mTitle.setText(event.getTitle());
            mDate.setText(event.getDate().toString()); //TODO: Maybe have this not be toString()?
            mLocation.setText("123 Placeholder Street."); //TODO: Hard code these as string in Event and UserEvent or use api
            mHost.setText(event.getOwnerFirstName() + " " + event.getOwnerLastName());
            mDescription.setText(event.getDescription());
        }
    }
}