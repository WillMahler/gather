package com.WKNS.gather.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
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

import com.WKNS.gather.EditProfileActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.User;
import com.google.gson.Gson;

import java.io.InputStream;

public class ProfileFragment extends Fragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    private User userObject;

    private ImageView mProfileImage;
    private TextView mName, mEmail, mPhoneNum, mBio;
    private Button mEditProfile, mLogout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userObject = ((MainActivity)getActivity()).getUserObject();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mProfileImage = root.findViewById(R.id.profileImage);
        mName = root.findViewById(R.id.name);
        mEmail = root.findViewById(R.id.email);
        mPhoneNum = root.findViewById(R.id.phoneNum);
        mBio = root.findViewById(R.id.bio_profileFragment);
        mEditProfile = root.findViewById(R.id.editProfile);
        mLogout = root.findViewById(R.id.logout);

        if(!userObject.getProfileImage().equals("")) {
            //new DownloadImageTask(mProfileImage).execute(userObject.getProfileImage());
            mProfileImage.setImageBitmap(userObject.getProfileBitmap());
        }
        else {
            mProfileImage.setImageResource(R.drawable.ic_baseline_person_24);
        }

        mName.setText((userObject.getFirstName() + "\n" + userObject.getLastName()));
        mEmail.setText(userObject.getEmail());

        if (userObject.getPhoneNum().isEmpty()) {
            mPhoneNum.setVisibility(View.GONE);
        }
        else {
            mPhoneNum.setText("+" + userObject.getPhoneNum());
        }

        if (userObject.getBio().isEmpty()) {
            mBio.setVisibility(View.GONE);
        }
        else {
            mBio.setText(userObject.getBio());
        }

        Linkify.addLinks(mEmail, Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(mPhoneNum, Linkify.PHONE_NUMBERS);

        // choose image button handler
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String userObjectString = gson.toJson(userObject);

                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("userObjectString", userObjectString);
                startActivity(intent);
            }
        });

        // log out button handler
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to log out?")
                        .setCancelable(true)
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

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
            bmImage.setImageBitmap(result);
        }
    }*/
}
