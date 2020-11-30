package com.WKNS.gather.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;

import com.WKNS.gather.UserProfileActivity;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.adapters.UserEventRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.adapters.UserRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.WKNS.gather.testData.User;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ArrayList<User> mUserList;
    private ArrayList<UserEvent> mEventList;
    private RecyclerView mRecyclerViewEvents, mRecyclerViewUsers;
    private UserRecyclerViewAdapter mAdapterPeople;
    private UserEventRecyclerViewAdapter mAdapterGatherings;
    private RecyclerView.LayoutManager mLayoutManagerPeople, mLayoutManagerEvents;
    private View mRoot;

    private com.WKNS.gather.ui.search.SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.search.SearchViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_search, container, false);

        mUserList = User.testUsers();
        mEventList = ((MainActivity)getActivity()).getmUserEvents();

        mRecyclerViewEvents = mRoot.findViewById(R.id.recyclerView_Search_Gatherings);
        mRecyclerViewEvents.setHasFixedSize(true);

        mLayoutManagerEvents = new LinearLayoutManager(mRoot.getContext());
        mRecyclerViewEvents.setLayoutManager(mLayoutManagerEvents);

        mAdapterGatherings = new UserEventRecyclerViewAdapter(mEventList);
        mRecyclerViewEvents.setAdapter(mAdapterGatherings);

        mAdapterGatherings.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("EVENT_ID", mEventList.get(position).eventID());
                startActivity(intent);
            }
        });

        mRecyclerViewUsers = mRoot.findViewById(R.id.recyclerView_Search_People);
        mRecyclerViewUsers.setHasFixedSize(true);

        mLayoutManagerPeople = new LinearLayoutManager(mRoot.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewUsers.setLayoutManager(mLayoutManagerPeople);

        mAdapterPeople = new UserRecyclerViewAdapter(mUserList);
        mRecyclerViewUsers.setAdapter(mAdapterPeople);

        mAdapterPeople.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        return mRoot;
    }
}