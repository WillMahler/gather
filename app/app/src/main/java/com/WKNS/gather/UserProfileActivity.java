package com.WKNS.gather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.helperMethods.DownloadImageTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    public static String TAG = UserProfileActivity.class.getSimpleName();

    private Toolbar actionbar;
    private ImageView mProfileImage;
    private TextView mName, mEmail, mPhoneNum, mBio;

    private FirebaseFirestore db;
    private CollectionReference ref;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mProfileImage = findViewById(R.id.profileImage);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPhoneNum = findViewById(R.id.phoneNum);
        mBio = findViewById(R.id.bio_userProfileActivity);

        db = FirebaseFirestore.getInstance();
        ref = db.collection("users");
        userID = getIntent().getStringExtra("attendeeID");

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_round_arrow_back_ios_24));

        loadUser();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadUser() {
        ref.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String photoURL = documentSnapshot.getString("profileImg");
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String email = documentSnapshot.getString("email");
                    String phoneNum = documentSnapshot.getString("phoneNum");
                    String bio = documentSnapshot.getString("bio");

                    if(!photoURL.isEmpty()) {
                        new DownloadImageTask(mProfileImage).execute(documentSnapshot.getString("profileImg"));
                    }
                    else {
                        mProfileImage.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                    mName.setText(firstName + "\n" + lastName);
                    mEmail.setText(email);

                    if (phoneNum.isEmpty()) {
                        mPhoneNum.setVisibility(View.GONE);
                    }
                    else {
                        mPhoneNum.setText("+" + phoneNum);
                    }

                    if (bio.isEmpty()) {
                        mBio.setVisibility(View.GONE);
                    }
                    else {
                        mBio.setText(bio);
                    }

                    Linkify.addLinks(mEmail, Linkify.EMAIL_ADDRESSES);
                    Linkify.addLinks(mPhoneNum, Linkify.PHONE_NUMBERS);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "User load failed.");
            }
        });
    }
}
