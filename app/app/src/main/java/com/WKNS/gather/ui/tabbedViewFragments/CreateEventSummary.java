package com.WKNS.gather.ui.tabbedViewFragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateEventSummary extends Fragment {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mTitle, mDate, mLocation, mDescription, mGuestListView;
    private Button mClearGuestList;

    private ArrayList<String> guestListArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_event_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTitle = view.findViewById(R.id.editText_createEvent_title);
        mDate = view.findViewById(R.id.editText_createEvent_date);
        mLocation = view.findViewById(R.id.editText_createEvent_location);
        mDescription = view.findViewById(R.id.editText_createEvent_description);
        mGuestListView = view.findViewById(R.id.editText_guest_list);
        mClearGuestList = view.findViewById(R.id.clear_guests);

        guestListArray = new ArrayList<>();

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

        mGuestListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showGuestDialog(v);
                }
            }
        });

        mGuestListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGuestDialog(v);
            }
        });

        mClearGuestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guestListArray.isEmpty()) {
                    Toast.makeText(getActivity(), "Guest list is already empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to clear the guest list?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                guestListArray.clear();
                                mGuestListView.setText("");
                                Toast.makeText(getActivity(), "Guest list cleared.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.ThemeOverlay_Material_Dialog_Alert,
                mDateSetListener,
                year, month, day
        );

        dialog.getDatePicker().setMinDate(new Date().getTime() - 1000);
        dialog.show();
    }

    private void showGuestDialog(View v) {
        LinearLayout container = new LinearLayout(getActivity());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);

        final EditText guest_email = new EditText(v.getContext());
        guest_email.setLayoutParams(lp);
        guest_email.setLayoutParams(lp);
        guest_email.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
        guest_email.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        guest_email.setLines(1);
        guest_email.setMaxLines(1);
        container.addView(guest_email, lp);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(R.string.label_add_guest);
        builder.setMessage(R.string.label_add_guest_message);
        builder.setView(container);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        // overriding the positive dialog button after show
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = guest_email.getText().toString().trim();
                if(email.isEmpty()) {
                    guest_email.setError("Email must be provided.");
                    guest_email.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    guest_email.setError("Please provide valid email.");
                    guest_email.requestFocus();
                }
                else if(guestListArray.contains(email)) {
                    guest_email.setError("Email already added.");
                    guest_email.requestFocus();
                }
                else {
                    guestListArray.add(email);
                    mGuestListView.setText(guestListArray.toString().substring(1, guestListArray.toString().length()-1));
                    Toast.makeText(getActivity(), "Guest added.", Toast.LENGTH_SHORT).show();
                    guest_email.setText("");
                    //dialog.dismiss();
                }
            }
        });
    }

    public boolean validateData() {
        String title = mTitle.getText().toString().trim();
        String date = mDate.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String description = mDescription.getText().toString().trim();
        String guestList = mGuestListView.getText().toString().trim();

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
        if (guestList.isEmpty()) {
            mGuestListView.setError("Guests are required.");
            //mGuestListView.requestFocus();
            return false;
        }
        return true;
    }

    /*
    *
    *
    *
    *
    * Write publish and draft functions here
    *
    *
    *
    *
     */
}
