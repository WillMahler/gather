package com.WKNS.gather.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;

public class ProfileFragment extends Fragment {

    private com.WKNS.gather.ui.profile.ProfileViewModel profileViewModel;
    private Button mLogout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.profile.ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mLogout = root.findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                ((MainActivity)getActivity()).logout();
            }
        });

        return root;
    }
}