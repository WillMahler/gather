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

import com.WKNS.gather.R;
import com.WKNS.gather.testData.Notification;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private ArrayList<Notification> mDataSet;
    private NotificationViewModel mNotificationViewModel;
    private RecyclerView mRecyclerView;
    private NotificationRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNotificationViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.notification.NotificationViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_notification, container, false);

        mDataSet = Notification.testData();

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_Notification);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NotificationRecyclerViewAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        setOnClickListeners(mAdapter);

        return mRoot;
    }

    private void setOnClickListeners(NotificationRecyclerViewAdapter adapter) {
        adapter.setmOnItemClickListener(new NotificationRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(int position) {
                Notification n = mDataSet.get(position);
                StringBuilder toastMessage = new StringBuilder();
                Notification.Type type = n.getType();
                toastMessage.append("Accepted ");

                if (type == Notification.Type.EVENT_INVITE) {
                    toastMessage.append("Event Invite to " + n.getmEventTitle());
                } else if (type == Notification.Type.FRIEND_REQUEST) {
                    toastMessage.append(n.getmRequesterFirstName() + "'s Friend Request");
                }

                Toast.makeText(mRoot.getContext(), toastMessage.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDeclineClick(int position) {
                Notification n = mDataSet.get(position);
                StringBuilder toastMessage = new StringBuilder();
                Notification.Type type = n.getType();
                toastMessage.append("Declined ");

                if (type == Notification.Type.EVENT_INVITE) {
                    toastMessage.append("Event Invite to " + n.getmEventTitle());
                } else if (type == Notification.Type.FRIEND_REQUEST) {
                    toastMessage.append(n.getmRequesterFirstName() + "'s Friend Request");
                }

                Toast.makeText(mRoot.getContext(), toastMessage.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}