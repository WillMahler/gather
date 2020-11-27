package com.WKNS.gather.ui.CreateEvent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import androidx.fragment.app.Fragment;

import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.databaseModels.Users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateEventFragment extends Fragment {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ArrayList<String> guestListArray;
    private Context context;
    private FirebaseFirestore db;
    private User userObject;
    private View mRoot;

    private EditText mTitle, mDate, mLocation, mDescription, mGuestListView;
    private Button mClearGuestList;
    private FloatingActionButton mFabCancel, mFabDone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        guestListArray = new ArrayList<>();
        context = this.getContext();
        db = FirebaseFirestore.getInstance();
        userObject = ((MainActivity) getActivity()).getUserObject();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_create_event, container, false);

        mTitle = mRoot.findViewById(R.id.editText_createEvent_title);
        mDate = mRoot.findViewById(R.id.editText_createEvent_date);
        mLocation = mRoot.findViewById(R.id.editText_createEvent_location);
        mDescription = mRoot.findViewById(R.id.editText_createEvent_description);
        mGuestListView = mRoot.findViewById(R.id.editText_guest_list);
        mClearGuestList = mRoot.findViewById(R.id.clear_guests);
        mFabCancel = mRoot.findViewById(R.id.fab_cancel);
        mFabDone = mRoot.findViewById(R.id.fab_done);

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

        mFabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to abandon this event?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEvent();
                                reset_activity();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Would you like to draft or publish this event?")
                        .setCancelable(true)
                        .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (validateData()) {
                                    try {
                                        addEvent(true);
                                        Toast.makeText(context, "Event Published",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Error: Event Not Published",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    reset_activity();
                                }
                            }
                        })
                        .setNegativeButton("Draft", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    addEvent(false);
                                    Toast.makeText(context, "Draft Saved",
                                            Toast.LENGTH_SHORT).show();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error: Draft Not Saved",
                                            Toast.LENGTH_SHORT).show();
                                }
                                reset_activity();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return mRoot;
    }

    private void reset_activity() {
        mTitle.setText("");
        mDate.setText("");
        mLocation.setText("");
        mDescription.setText("");
        mGuestListView.setText("");
        guestListArray.clear();
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

    private boolean validateData() {
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

    private boolean addEvent(boolean isPublished) throws ParseException {
        // Owner Details
        String ownerID = userObject.getUserID();
        String ownerFirst = userObject.getFirstName();
        String ownerLast = userObject.getLastName();

        // Event Details
        String title = mTitle.getText().toString().trim();
        String dateString = mDate.getText().toString().trim();;
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        String location = mLocation.getText().toString().trim();
        String description = mDescription.getText().toString().trim();

        // Instantiate New Event Object
        com.WKNS.gather.databaseModels.Events.Event event = new Event(title, description, ownerID, ownerFirst,
                ownerLast, date, location, isPublished);

        // String eventID = intent.getStringExtra("eventID");
        String eventID = "";

        // Add event to events collection, if it doesn't already exist.
        // Otherwise, update the event document.
        if (eventID.isEmpty()) { // Initial Event Creation
            db.collection("events")
                    .add(event)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("CreateEventSummary",
                                    "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("CreateEventSummary", "Error writing document", e);
                        }
                    });
        }
        else { // Event Edit
            db.collection("events").document(eventID)
                    .set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("CreateEventSummary", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("CreateEventSummary", "Error writing document", e);
                        }
                    });
        }

        return true;
    }

    private boolean deleteEvent() {
        // Owner + Event Details
        // String eventID = intent.getStringExtra("eventID");
        String eventID = "";

        // If event is in database, delete. Do nothing otherwise.
        if (!eventID.isEmpty()) {
            db.collection("events").document(eventID)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("CreateEventSummary", "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("CreateEventSummary", "Error deleting document", e);
                        }
                    });

        }
        return false;
    }
}
