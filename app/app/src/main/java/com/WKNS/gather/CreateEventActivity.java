package com.WKNS.gather;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.databaseModels.Events.Attendee;
import com.WKNS.gather.databaseModels.Events.Event;
import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.helperMethods.DownloadImageTask;
import com.WKNS.gather.recyclerViews.adapters.GuestListRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnRemoveClickListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    public static String TAG = CreateEventActivity.class.getSimpleName();

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ArrayList<String> guestListArray;
    private Context context;
    private Gson gson;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private FirebaseFunctions mFunctions;
    private RecyclerView mRecyclerView;
    private GuestListRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private User userObject;
    private Uri imageURI;
    private String imageURL;
    private Event event;
    private String eventID;

    private Toolbar actionbar;
    private ImageView mEventImage;
    private EditText mTitle, mDate, mLocation, mDescription, mGuestListView;
    private Button mClearGuestList;
    private FloatingActionButton mFabCancel, mFabDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("New Gathering");

        mEventImage = findViewById(R.id.imageView_createEvent_displayPic);
        mTitle = findViewById(R.id.editText_createEvent_title);
        mDate = findViewById(R.id.editText_createEvent_date);
        mLocation = findViewById(R.id.editText_createEvent_location);
        mDescription = findViewById(R.id.editText_createEvent_description);
        mGuestListView = findViewById(R.id.editText_guest_list);
        mClearGuestList = findViewById(R.id.clear_guests);
        mFabCancel = findViewById(R.id.fab_cancel);
        mFabDone = findViewById(R.id.fab_done);

        guestListArray = new ArrayList<>();
        context = this;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        mFunctions = FirebaseFunctions.getInstance();
        gson = new Gson();

        String userObjectString = getIntent().getStringExtra("userObjectString");
        userObject = gson.fromJson(userObjectString, User.class);
        imageURL = "";

        mEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        eventID = getIntent().getStringExtra("eventID");

        // Initialize Event if editing + Populate Text Fields
        if (!eventID.isEmpty()) {
            db.collection("events")
                    .document(eventID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (task.isSuccessful()) {
                                if (document.exists()) {
                                    event = document.toObject(Event.class);
                                    event.setID(document.getId());
                                    populateFields();
                                }
                                else {
                                    Log.d("CreateEventActivity", "No such event");
                                }
                            }
                            else {
                                Log.d("CreateEventActivity", "get failed with ", task.getException());
                            }
                        }
                    });
        }

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
                    Toast.makeText(context, "Guest list is already empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to clear the guest list?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                guestListArray.clear();
                                mAdapter.notifyDataSetChanged();
                                mGuestListView.setText("");
                                Toast.makeText(context, "Guest list cleared.", Toast.LENGTH_SHORT).show();
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
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
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
                                        if(imageURI != null) {
                                            uploadPictureToDB(imageURI, true);
                                        }
                                        else {
                                            addEvent(true);
                                        }
                                        Toast.makeText(context, "Event Published", Toast.LENGTH_SHORT).show();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Error: Event Not Published", Toast.LENGTH_SHORT).show();
                                    }
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Draft", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if(imageURI != null) {
                                        uploadPictureToDB(imageURI, false);
                                    }
                                    if (!eventID.isEmpty() && event.isPublished()) {
                                        addEvent(true);
                                        Toast.makeText(context, "Published Event Cannot Be Drafted - Event Published", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        addEvent(false);
                                        Toast.makeText(context, "Draft Saved", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Error: Draft Not Saved", Toast.LENGTH_SHORT).show();
                                }
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView_GuestList);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GuestListRecyclerViewAdapter(guestListArray);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnRemoveClickListener() {
            @Override
            public void onRemoveClick(int position) {
                guestListArray.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        mFabCancel.callOnClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode==RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            mEventImage.setPadding(0, 0, 0, 0);
            mEventImage.setImageURI(imageURI);
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void uploadPictureToDB(Uri file, final boolean isPublished) {
        final String randomKey = UUID.randomUUID().toString();
        final StorageReference ref = mStorageRef.child("profile_images/" + randomKey);
        UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    imageURL = task.getResult().toString();
                    try {
                        addEvent(isPublished);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d(TAG, "Image upload failed.");
                }
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.ThemeOverlay_Material_Dialog_Alert,
                mDateSetListener,
                year, month, day
        );

        dialog.getDatePicker().setMinDate(new Date().getTime() - 1000);
        dialog.show();
    }

    private void showGuestDialog(View v) {
        LinearLayout container = new LinearLayout(context);
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

        final Drawable checkMark = (Drawable)getResources().getDrawable(R.drawable.ic_baseline_check_green_24);
        checkMark.setBounds(0, 0, checkMark.getIntrinsicWidth(), checkMark.getIntrinsicHeight());

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
                final String email = guest_email.getText().toString().trim();
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
                    // Validate whether user exists
                    mAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean userExists = !task.getResult().getSignInMethods().isEmpty();

                                    if (!userExists) {
                                        guest_email.setError("Email not registered with Gather.");
                                        guest_email.requestFocus();
                                    } else {
                                        guest_email.setError("Added " + email, checkMark);
                                        guest_email.requestFocus();
                                        guestListArray.add(email);
                                        mAdapter.notifyDataSetChanged();
                                        guest_email.setText("");
                                    }
                                }
                            });
                }
            }
        });
    }

    private void populateFields() {
        if(!event.getPhotoURL().equals("")) {
            mEventImage.setPadding(0, 0, 0, 0);
            new DownloadImageTask(mEventImage).execute(event.getPhotoURL());
        }
        mTitle.setText(event.getTitle());
        mLocation.setText(event.getLocation());
        mDescription.setText(event.getDescription());

        mDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(event.getDate()));

        db.collection("events")
                .document(event.getEventID())
                .collection("attendees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Attendee attendee = document.toObject(Attendee.class);
                                if (!attendee.getEmail().equals(userObject.getEmail())) {
                                    guestListArray.add(attendee.getEmail());
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        else {
                            Log.d("CreateEventActivity", "Error: ", task.getException());
                        }
                    }
                });

        // mAdapter.notifyDataSetChanged();
    }

    private boolean validateData() {
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
        if (guestListArray.isEmpty()) {
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
        String dateString = mDate.getText().toString().trim();
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        String location = mLocation.getText().toString().trim();
        String description = mDescription.getText().toString().trim();

        // Instantiate New Event Object
        com.WKNS.gather.databaseModels.Events.Event event = new Event(title, description, ownerID, ownerFirst,
                ownerLast, date, location, isPublished, imageURL);

        // Add event to events collection, if it doesn't already exist.
        // Otherwise, update the event document.
        if (eventID.isEmpty()) { // Initial Event Creation
            db.collection("events")
                    .add(event)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG,
                                    "DocumentSnapshot written with ID: " + documentReference.getId());
                            eventID = documentReference.getId();
                            sendInvites(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
        else { // Event Edit
            db.collection("events").document(eventID)
                    .set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }

        return true;
    }

    private Task<String> sendInvites(boolean newEvent) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();

        data.put("eventID", eventID);
        data.put("invites", gson.toJson(guestListArray));

        if (newEvent) {
            data.put("newEvent", true);
        } else {
            data.put("newEvent", false);
        }

        Log.d(TAG, "sendInvites() for eventID: " + eventID);

        return mFunctions
                .getHttpsCallable("sendInvites")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        Log.d(TAG, "sendInvites() result: " + result);
                        return result;
                    }
                });
    }

    private boolean deleteEvent() {
        // If event is in database, delete. Do nothing otherwise.
        if (!eventID.isEmpty()) {
            db.collection("events").document(eventID)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
        return false;
    }
}