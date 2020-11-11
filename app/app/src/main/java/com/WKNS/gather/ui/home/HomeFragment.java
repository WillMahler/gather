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

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ArrayList<UserEvent> mUserEvents;
    private HomeViewModel mHomeViewModel;
    private RecyclerView mRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View mRoot;
    private FirebaseFirestore db;
    private CollectionReference userCollections;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);

        db = ((MainActivity)getActivity()).getFireStoreDB();

        mRecyclerView = mRoot.findViewById(R.id.recyclerView_Home);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mRoot.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mUserEvents = new ArrayList<UserEvent>();
        mAdapter = new HomeRecyclerViewAdapter(mUserEvents);
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
                intent.putExtra("EXTRA_SESSION_ID", mUserEvents.get(position).eventID());
                startActivity(intent);
            }
        });

        //TODO: BATCH the requests for events, limit it to 15 most recent events??
        userCollections = db.collection("users").document(((MainActivity)getActivity()).getUserFireBase().getUid())
                .collection("userEvents");

        userCollections.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                int sizeMUserevents = mUserEvents.size();
                int addedDocuments = 0;
                for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                        //Log.d(TAG, "New city:" + change.getDocument().getData());
                    }
                    UserEvent retrieved = change.getDocument().toObject(UserEvent.class);
                    retrieved.eventID(change.getDocument().getReference().getId());
                    Log.d("nick scott", retrieved.eventID());
                    mUserEvents.add(retrieved);
                    addedDocuments++;
                    String source = querySnapshot.getMetadata().isFromCache() ?
                            "local cache" : "server";

                    Log.d("FETCH ", "Data fetched from " + source);
                }
                mAdapter.notifyItemRangeInserted(sizeMUserevents, addedDocuments);
            }
        });;
        return mRoot;
    }
}