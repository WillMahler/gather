package com.WKNS.gather.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.adapters.UserEventRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeDraftsFragment extends Fragment {
    public static String TAG = HomeDraftsFragment.class.getSimpleName();

    private ArrayList<UserEvent> mUserEvents;
    private RecyclerView mRecyclerView;
    private UserEventRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserEvents = ((MainActivity)getActivity()).getmUserEvents();

        // TODO Need to actually clean this up so that this pulls drafts only
        ((MainActivity)getActivity()).setHomeFragmentRefreshListener(new MainActivity.HomeFragmentRefreshListener() {
            @Override
            public void onRefresh(ArrayList<UserEvent> userEvents) {
                mUserEvents.clear();
                mUserEvents.addAll(userEvents);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_home_tab_drafts, container, false);

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_home_drafts);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new UserEventRecyclerViewAdapter(mUserEvents);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("EVENT_ID", mUserEvents.get(position).eventID());
                startActivity(intent);
            }
        });

        return mRoot;
    }
}
