package com.WKNS.gather.ui.tabbedViewFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.InputStream;

public class EventDetailsSummary extends Fragment {
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

            if(!event.getPhotoURL().isEmpty()) {
                new DownloadImageTask(mDisplayPic).execute(event.getPhotoURL());
            }

            mTitle.setText(event.getTitle());
            mDate.setText(event.getDate().toString()); //TODO: Maybe have this not be toString()?
            mLocation.setText("123 Placeholder Street."); //TODO: Hard code these as string in Event and UserEvent or use api
            mHost.setText(event.getOwnerFirstName() + " " + event.getOwnerLastName());
            mDescription.setText(event.getDescription());
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setPadding(0, 0, 0, 0);
            bmImage.setImageBitmap(result);
        }
    }
}