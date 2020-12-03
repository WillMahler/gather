package com.WKNS.gather.ui.tabbedViewFragments;

import android.content.Intent;
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

import com.WKNS.gather.CreateEventActivity;
import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.databaseModels.Users.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class EventDetailsSummary extends Fragment {
    private Event mEventObj;
    private ImageView mDisplayPic;
    private TextView mTitle, mDate, mLocation, mHost, mDescription;
    private FloatingActionButton mFAB;
    private User userObject;

    public EventDetailsSummary(Event event) {
        mEventObj = event;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userObject = ((EventDetailsActivity) getActivity()).getUserObject();
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

        mFAB = view.findViewById(R.id.floatingActionButton);
        mFAB.hide();

        mDisplayPic.setImageResource(R.drawable.ic_baseline_video_library_24);
        super.onViewCreated(view, savedInstanceState);

        setEventDetails(mEventObj);
        displayFAB(mEventObj);
    }

    public void setEventDetails(Event event) {
        if (event != null) {
            mEventObj = event;
            mTitle.setText(event.getTitle());
            mDate.setText(event.getDate().toString()); //TODO: Maybe have this not be toString()?
            mLocation.setText("123 Placeholder Street."); //TODO: Hard code these as string in Event and UserEvent or use api
            mHost.setText(event.getOwnerFirstName() + " " + event.getOwnerLastName());
            mDescription.setText(event.getDescription());
        }
    }

    public void displayFAB(final Event event) {
        if (event != null) {
            /* Hide editing button if the current user is not the owner of the event, otherwise
             * the button should launch CreateEventActivity */
            if (event.getOwnerID().equals(userObject.getUserID())) {
                mFAB.show();
                mFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View root) {
                        Intent intent = new Intent(getActivity(), CreateEventActivity.class);

                        Gson gson = new Gson();
                        String userObjectString = gson.toJson(userObject);
                        intent.putExtra("userObjectString", userObjectString);
                        intent.putExtra("eventID", event.getEventID());

                        startActivity(intent);
                    }
                });
            }
        }
    }
}
