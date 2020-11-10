package com.WKNS.gather.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    public static final String TAG = "TAG";

    //private com.WKNS.gather.ui.profile.ProfileViewModel profileViewModel;
    private ImageView mProfileImage;
    private TextView firstName, lastName, email;
    private Button mChooseImage, mLogout;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private GoogleSignInAccount signInAccount;
    private User userObject;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //profileViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.profile.ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mProfileImage = root.findViewById(R.id.profileImage);
        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        email = root.findViewById(R.id.email);
        mChooseImage = root.findViewById(R.id.chooseImage);
        mLogout = root.findViewById(R.id.logout);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        userObject = ((MainActivity)getActivity()).getUserObject();

        // populating fragment with user data
        /*if(!userObject.getProfileImage().isEmpty()) {
            Picasso.get().load(userObject.getProfileImage()).into(mProfileImage);
        }*/
        mProfileImage.setImageResource(R.drawable.ic_baseline_person_24);
        firstName.setText(userObject.getFirstName());
        lastName.setText(userObject.getLastName());
        email.setText(userObject.getEmail());

        if(signInAccount!= null) {
            mChooseImage.setVisibility(View.GONE);
        }

        // choose image button handler
        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        // log out button handler
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity)getActivity()).logout();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode==RESULT_OK && data != null && data.getData() != null) {
            Uri imageURI = data.getData();
            mProfileImage.setImageURI(imageURI);
            uploadPictureToDB(imageURI);
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
                    updateUserProfilePhoto(task.getResult().toString());
                }
                else {
                    // Handle failures
                }
            }
        });
    }

    private void updateUserProfilePhoto(String downloadUrl) {
        userObject.setProfileImage(downloadUrl);
        DocumentReference documentReference = db.collection("users").document(userObject.getUserID());
        documentReference.update("profileImg", downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "user document update success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "user document update failed: " + e.toString());
            }
        });
    }
}