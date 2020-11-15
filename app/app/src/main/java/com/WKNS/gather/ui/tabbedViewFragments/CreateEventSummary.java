package com.WKNS.gather.ui.tabbedViewFragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.WKNS.gather.CreateEventActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.databaseModels.Users.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import org.w3c.dom.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEventSummary extends Fragment {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mTitle, mDate, mLocation, mDescription;
    private FirebaseFirestore db;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intent = ((CreateEventActivity) getActivity()).getIntent();
        db = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_create_event_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTitle = view.findViewById(R.id.editText_createEvent_title);
        mDate = view.findViewById(R.id.editText_createEvent_date);
        mLocation = view.findViewById(R.id.editText_createEvent_location);
        mDescription = view.findViewById(R.id.editText_createEvent_description);

        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePicker();
                }
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                mDate.setText(date);
            }
        };
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog,
                mDateSetListener,
                year, month, day
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public boolean validateData() {
        String title = mTitle.getText().toString().trim();
        String date = mDate.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String description = mDescription.getText().toString().trim();

        if (title.isEmpty()) {
            mTitle.setError("Title is required.");
            mTitle.requestFocus();
            return false;
        }
        if (date.isEmpty()) {
            mDate.setError("Date is required.");
            //mDate.requestFocus();
            return false;
        }
        if (location.isEmpty()) {
            mLocation.setError("Location is required.");
            mLocation.requestFocus();
            return false;
        }
        if (description.isEmpty()) {
            mDescription.setError("Description is required.");
            mDescription.requestFocus();
            return false;
        }
        return true;
    }

    public boolean addEvent(boolean isPublished) throws ParseException {
        // Owner Details
        String ownerID = intent.getStringExtra("userID");
        String ownerFirst = intent.getStringExtra("userFirst");
        String ownerLast = intent.getStringExtra("userLast");

        // Event Details
        String title = mTitle.getText().toString().trim();
        String dateString = mDate.getText().toString().trim();;
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        String location = mLocation.getText().toString().trim();
        String description = mDescription.getText().toString().trim();

        // Instantiate New Event Object
        Event event = new Event(title, description, ownerID, ownerFirst,
                ownerLast, date, location, isPublished);

        String eventID = intent.getStringExtra("eventID");

        // Add event to events collection, if it doesn't already exist.
        // Otherwise, update the event document.
        if (eventID.isEmpty()) { // Initial Event Creation
            db.collection("events").add(event);
        }
        else { // Event Edit
            db.collection("events").document(eventID).set(event);
        }

        return true;
    }

    public boolean deleteEvent() {
        // Owner + Event Details
        String eventID = intent.getStringExtra("eventID");

        // If event is in database, delete. Do nothing otherwise.
        if (!eventID.isEmpty()) {
            db.collection("events").document(eventID).delete();
            return true;
        }

        return false;
    }
}
