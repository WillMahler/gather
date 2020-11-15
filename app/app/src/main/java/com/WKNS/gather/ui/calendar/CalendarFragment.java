package com.WKNS.gather.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.CreateEventActivity;
import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;

import com.WKNS.gather.databaseModels.Users.User;
import com.WKNS.gather.testData.Event;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    private ArrayList<Event> mDataList;
    private CalendarViewModel mCalendarViewModel;
    private RecyclerView mRecyclerView;
    private CalendarRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;
    private FloatingActionButton mFAB;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCalendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_calendar, container, false);

        mDataList = Event.testEvents();

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_Calendar);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CalendarRecyclerViewAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CalendarRecyclerViewAdapter.OnItemClickListener() {
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

        mFAB = mRoot.findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);

                User userObject = ((MainActivity) getActivity()).getUserObject();
                intent.putExtra("userID", userObject.getUserID());
                intent.putExtra("userFirst", userObject.getFirstName());
                intent.putExtra("userLast", userObject.getLastName());
                intent.putExtra("eventID", "");

                startActivity(intent);
            }
        });

        return mRoot;
    }
}