package com.WKNS.gather.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.adapters.UserEventRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.WKNS.gather.testData.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static String TAG = HomeFragment.class.getSimpleName();
    
    private ArrayList<UserEvent> mUserEvents;
    private HomeViewModel mHomeViewModel;
    private RecyclerView mRecyclerView;
    private UserEventRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;
    private FirebaseFirestore db;
    private CollectionReference userEventsCollection;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserEvents = ((MainActivity)getActivity()).getmUserEvents();

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
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_Home);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new UserEventRecyclerViewAdapter(mUserEvents);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("EVENT_ID", mUserEvents.get(position).getEventID());
                startActivity(intent);
            }
        });

        return mRoot;
    }
}
