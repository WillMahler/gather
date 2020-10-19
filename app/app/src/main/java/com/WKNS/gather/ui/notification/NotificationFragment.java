package com.WKNS.gather.ui.notification;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.WKNS.gather.R;

public class NotificationFragment extends Fragment {

    private com.WKNS.gather.ui.notification.NotificationViewModel notificationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.notification.NotificationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }
}