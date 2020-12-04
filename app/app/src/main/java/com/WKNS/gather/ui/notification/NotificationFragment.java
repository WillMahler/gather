package com.WKNS.gather.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.adapters.InviteRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnInviteClickListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private ArrayList<UserEvent> mDataSet;
    private NotificationViewModel mNotificationViewModel;
    private RecyclerView mRecyclerView;
    private InviteRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNotificationViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.notification.NotificationViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_notification, container, false);

        // Filter user's events to only show events where user's status is pending
        mDataSet = filterData();

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_Notification);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new InviteRecyclerViewAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        setOnClickListeners(mAdapter);

        return mRoot;
    }

    private void setOnClickListeners(InviteRecyclerViewAdapter adapter) {
        adapter.setmOnItemClickListener(new OnInviteClickListener() {
            @Override
            public void onAcceptClick(int position) {
                UserEvent n = mDataSet.get(position);
                String toastMessage = "Accepted Event Invite to " + n.getTitle();
                Toast.makeText(mRoot.getContext(), toastMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDeclineClick(int position) {
                UserEvent n = mDataSet.get(position);
                String toastMessage = "Declined Event Invite to " + n.getTitle();
                Toast.makeText(mRoot.getContext(), toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<UserEvent> filterData() {
        ArrayList<UserEvent> invites = new ArrayList<>();

        for (UserEvent userEvent : ((MainActivity) getActivity()).getmUserEvents()) {
            if (userEvent.getStatus() == 0) { // Then user has responded already
                invites.add(userEvent);
            }
        }

        return invites;
    }

}