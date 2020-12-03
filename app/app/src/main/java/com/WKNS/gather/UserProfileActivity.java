package com.WKNS.gather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar actionbar;
    private ImageView mProfileImage;
    private TextView mName, mEmail, mPhoneNum, mBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mProfileImage = findViewById(R.id.profileImage);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPhoneNum = findViewById(R.id.phoneNum);
        mBio = findViewById(R.id.bio);

        // TODO Remove these later, this is just for display for the D2 demo
        mName.setText("Gordon\nRamsay");
        mProfileImage.setImageResource(R.drawable.ic_baseline_person_24);

        // setting title of action bar
        actionbar = findViewById(R.id.actionbar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_round_arrow_back_ios_24));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
