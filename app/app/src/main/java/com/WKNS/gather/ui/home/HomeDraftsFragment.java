package com.WKNS.gather.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.WKNS.gather.EventDetailsActivity;
import com.WKNS.gather.MainActivity;
import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.adapters.UserEventRecyclerViewAdapter;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private ListenerRegistration mListenerRegistration;
    private CollectionReference mUserEventsCollection;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_home_tab_drafts, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        mUserEvents = new ArrayList<>();

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
                intent.putExtra("EVENT_ID", mUserEvents.get(position).getEventID());

                Gson gson = new Gson();
                String userObjectString = gson.toJson(((MainActivity) getActivity()).getUserObject());
                intent.putExtra("USER_STR", userObjectString);

                startActivity(intent);
            }
        });

        return mRoot;
    }


    @Override
    public void onResume() {
        super.onResume();
        attachListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachListener();
    }

    private void attachListener() {

        mUserEventsCollection = mDB.collection("users").document(mAuth.getUid())
                .collection("userEvents");
        mListenerRegistration = mUserEventsCollection.whereEqualTo("status", 1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "listenUserEventsAccepted(): Listen failed.", e);
                    return;
                }

                mUserEvents.clear();

                for (QueryDocumentSnapshot doc : value) {
                    UserEvent newEvent = doc.toObject(UserEvent.class);
                    newEvent.setEventID(doc.getId());
                    newEvent.setPublished(doc.getBoolean("published"));
                    if (!newEvent.isPublished()) {
                        mUserEvents.add(newEvent);
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void detachListener() {
        mListenerRegistration.remove();
    }
}
