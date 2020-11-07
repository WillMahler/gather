package com.WKNS.gather.ui.home;

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
import com.WKNS.gather.testData.Event;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<Event> mDataList;
    private HomeViewModel mHomeViewModel;
    private RecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);

        mDataList = Event.testEvents();

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_Home);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HomeRecyclerViewAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /* TODO:
                    This is where we would want to pass the Event at `position` in the dataset
                    to the specific event page, so that it gets the info to display from
                    the Event instance. This setup just opens it generically.
                 */
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                startActivity(intent);
            }
        });

        return mRoot;
    }
}