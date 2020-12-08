package com.WKNS.gather.ui.notification;

import android.content.Intent;
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

import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.adapters.InviteRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnInviteClickListener;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private ArrayList<UserEvent> mDataSet;
    private NotificationViewModel mNotificationViewModel;
    private RecyclerView mRecyclerView;
    private InviteRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;
    private FirebaseFirestore db;
    private User userObject;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userObject = ((MainActivity) getActivity()).getUserObject();
        mDataSet = ((MainActivity)getActivity()).getUserEventsInvited();

        ((MainActivity)getActivity()).setNotificationFragmentRefreshListener(new MainActivity.NotificationFragmentRefreshListener() {
            @Override
            public void onRefresh(ArrayList<UserEvent> userEvents) {
                mDataSet.clear();
                mDataSet.addAll(userEvents);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNotificationViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.notification.NotificationViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_notification, container, false);

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_Notification);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new InviteRecyclerViewAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        db = FirebaseFirestore.getInstance();

        setOnClickListeners(mAdapter);

        return mRoot;
    }

    private void setOnClickListeners(InviteRecyclerViewAdapter adapter) {

        adapter.setCardClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("EVENT_ID", mDataSet.get(position).getEventID());

                Gson gson = new Gson();
                String userObjectString = gson.toJson(((MainActivity) getActivity()).getUserObject());
                intent.putExtra("USER_STR", userObjectString);

                startActivity(intent);
            }
        });

        adapter.setmOnItemClickListener(new OnInviteClickListener() {
            @Override
            public void onAcceptClick(int position) {
                UserEvent event = mDataSet.get(position);
                String toastMessage = "Accepted Event Invite to " + event.getTitle();
                Toast.makeText(mRoot.getContext(), toastMessage, Toast.LENGTH_LONG).show();

                event.setStatus(1);

                db.collection("users")
                  .document(userObject.getUserID())
                  .collection("userEvents")
                  .document(event.getEventID())
                  .set(event, SetOptions.merge());
            }

            @Override
            public void onDeclineClick(int position) {
                UserEvent event = mDataSet.get(position);
                String toastMessage = "Declined Event Invite to " + event.getTitle();
                Toast.makeText(mRoot.getContext(), toastMessage, Toast.LENGTH_LONG).show();

                event.setStatus(2);

                db.collection("users")
                        .document(userObject.getUserID())
                        .collection("userEvents")
                        .document(event.getEventID())
                        .set(event, SetOptions.merge());
            }
        });
    }

}