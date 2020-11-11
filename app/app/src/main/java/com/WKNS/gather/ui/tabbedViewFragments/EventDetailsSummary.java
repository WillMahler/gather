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

import com.WKNS.gather.R;

public class EventDetailsSummary extends Fragment {

    ImageView mDisplayPic;
    TextView mTitle, mDate, mLocation, mHost, mDescription;

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
        mTitle.setText("Borat Watchparty");
        mDate.setText("November 13, 2020");
        mLocation.setText("Great Nation of Kazakhstan");
        mHost.setText("Hosted by: Azamat Bagatov");
        mDescription.setText("Borat Subsequent Moviefilm: Delivery of Prodigious Bribe to American Regime for Make Benefit Once Glorious Nation of Kazakhstan");

        super.onViewCreated(view, savedInstanceState);
    }
}
