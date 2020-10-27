package com.WKNS.gather.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.WKNS.gather.CreateEventActivity;
import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private Button button;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        button = root.findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}