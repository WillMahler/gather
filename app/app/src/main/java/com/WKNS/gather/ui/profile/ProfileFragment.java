package com.WKNS.gather.ui.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;

public class ProfileFragment extends Fragment {

    private com.WKNS.gather.ui.profile.ProfileViewModel profileViewModel;
    private ImageView mProfileImage;
    private TextView firstName, lastName;
    private Button mChooseImage, mLogout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.profile.ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mProfileImage = root.findViewById(R.id.profileImage);
        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        mChooseImage = root.findViewById(R.id.chooseImage);
        mLogout = root.findViewById(R.id.logout);

        firstName.setText(((MainActivity)getActivity()).getUserObject().getFirstName());
        lastName.setText(((MainActivity)getActivity()).getUserObject().getLastName());

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ...
            }
        });

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
}