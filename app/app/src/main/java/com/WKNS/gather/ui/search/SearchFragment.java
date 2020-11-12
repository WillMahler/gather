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
import com.WKNS.gather.R;

import com.WKNS.gather.UserProfileActivity;
import com.WKNS.gather.testData.Event;
import com.WKNS.gather.testData.User;
import com.google.api.Distribution;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ArrayList<User> mUserList;
    private ArrayList<Event> mEventList;
    private RecyclerView mRecyclerViewEvents, mRecyclerViewUsers;
    private SearchRecyclerViewAdapterPeople mAdapterPeople;
    private SearchRecyclerViewAdapterGatherings mAdapterGatherings;
    private RecyclerView.LayoutManager mLayoutManagerPeople, mLayoutManagerEvents;
    private View mRoot;

    private com.WKNS.gather.ui.search.SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this).get(com.WKNS.gather.ui.search.SearchViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_search, container, false);

        mUserList = User.testUsers();
        mEventList = Event.testEvents();

        mRecyclerViewEvents = mRoot.findViewById(R.id.recyclerView_Search_Gatherings);
        mRecyclerViewEvents.setHasFixedSize(true);

        mLayoutManagerEvents = new LinearLayoutManager(mRoot.getContext());
        mRecyclerViewEvents.setLayoutManager(mLayoutManagerEvents);

        mAdapterGatherings = new SearchRecyclerViewAdapterGatherings(mEventList);
        mRecyclerViewEvents.setAdapter(mAdapterGatherings);

        mAdapterGatherings.setOnItemClickListener(new SearchRecyclerViewAdapterGatherings.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerViewUsers = mRoot.findViewById(R.id.recyclerView_Search_People);
        mRecyclerViewUsers.setHasFixedSize(true);

        mLayoutManagerPeople = new LinearLayoutManager(mRoot.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewUsers.setLayoutManager(mLayoutManagerPeople);

        mAdapterPeople = new SearchRecyclerViewAdapterPeople(mUserList);
        mRecyclerViewUsers.setAdapter(mAdapterPeople);

        mAdapterPeople.setOnItemClickListener(new SearchRecyclerViewAdapterPeople.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        return mRoot;
    }
}