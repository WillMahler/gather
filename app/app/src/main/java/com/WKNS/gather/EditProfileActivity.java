package com.WKNS.gather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private User userObject;
    private Uri imageURI;

    private Toolbar actionbar;
    private ImageView mProfileImage;
    private TextView mPhoneNum, mBio;
    private ProgressBar mProgress;
    private Button mSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_round_arrow_back_ios_24));

        mProfileImage = findViewById(R.id.profileImage);
        mPhoneNum = findViewById(R.id.editText_phone_number);
        mBio = findViewById(R.id.editText_bio);
        mProgress = findViewById(R.id.progressBar);
        mSave = findViewById(R.id.save_button);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        Gson gson = new Gson();
        final String userObjectString = getIntent().getStringExtra("userObjectString");
        userObject = gson.fromJson(userObjectString, User.class);

        mProfileImage.setImageBitmap(userObject.getProfileBitmap());
        mPhoneNum.setText(userObject.getPhoneNum());
        mBio.setText(userObject.getBio());

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = mPhoneNum.getText().toString().trim();
                String bio = mBio.getText().toString().trim();

                if (imageURI == null && phoneNum.equals(userObject.getPhoneNum()) && bio.equals(userObject.getBio())) {
                    Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                if (!phoneNum.isEmpty() && !android.util.Patterns.PHONE.matcher(phoneNum).matches()) {
                    mPhoneNum.setError("Please enter valid phone number.");
                    mPhoneNum.requestFocus();
                    return;
                }

                if (imageURI == null) {
                    mProgress.setVisibility(View.VISIBLE);
                    updateUserProfile("", phoneNum, bio);
                    return;
                }

                mProgress.setVisibility(View.VISIBLE);
                uploadPictureToDB(imageURI);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode==RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            mProfileImage.setImageURI(imageURI);
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void uploadPictureToDB(Uri file) {
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
                    updateUserProfile(task.getResult().toString(), mPhoneNum.getText().toString().trim(), mBio.getText().toString().trim());
                }
                else {
                    // Handle failures
                }
            }
        });
    }

    private void updateUserProfile(String photoUrl, String phoneNum, String bio) {
        DocumentReference documentReference = db.collection("users").document(userObject.getUserID());
        if(!photoUrl.equals("")) {
            documentReference.update("profileImg", photoUrl);
        }
        documentReference.update("phoneNum", phoneNum);
        documentReference.update("bio", bio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "user document update success");
                Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "user document update failed: " + e.toString());
                Toast.makeText(getApplicationContext(), "User profile update failed: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
