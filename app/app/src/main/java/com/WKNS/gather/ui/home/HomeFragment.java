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
    private ArrayList<UserEvent> mUserEvents;
    private HomeViewModel mHomeViewModel;
    private RecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;
    private FirebaseFirestore db;
    private CollectionReference userEventsCollection;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserEvents = ((MainActivity)getActivity()).getmUserEvents();

        ((MainActivity)getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
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

        mAdapter = new HomeRecyclerViewAdapter(mUserEvents);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("Nick ", "adapter setup " + mUserEvents.size());
        mAdapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                /* TODO:
                    This is where we would want to pass the Event at `position` in the dataset
                    to the specific event page, so that it gets the info to display from
                    the Event instance. This setup just opens it generically.
                 */
                Bundle b = new Bundle();
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", mUserEvents.get(position).eventID());
                startActivity(intent);
            }
        });
        return mRoot;
    }

    public void notifyChange(ArrayList<UserEvent> userEvents) {
        Log.d("Nick ", "adding " + userEvents.size() + " events");
        mUserEvents.clear();
        mUserEvents.addAll(userEvents);
        mAdapter.notifyDataSetChanged();
        //mAdapter.notifyItemRangeInserted(0, userEvents.size());
    }

    public FirebaseFirestore getFireStoreDB() {return db; }
}