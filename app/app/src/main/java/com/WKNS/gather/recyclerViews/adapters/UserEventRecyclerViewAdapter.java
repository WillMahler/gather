package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.EventViewHolder;
import com.WKNS.gather.testData.Event;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserEventRecyclerViewAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private ArrayList<UserEvent> mEvents;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public UserEventRecyclerViewAdapter(ArrayList<UserEvent> events) {
        mEvents = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_search_gatherings, parent, false);
        return new EventViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        UserEvent e = mEvents.get(position);

        holder.mImageView.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
        holder.mTextViewTitle.setText(e.getTitle());
        holder.mTextViewHost.setText("Hosted by: " + e.getOwnerFirstName());
        holder.mTextViewDate.setText(e.getDate().toString());;
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}

