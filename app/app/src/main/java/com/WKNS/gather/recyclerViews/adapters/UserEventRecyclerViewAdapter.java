package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.WKNS.gather.R;
import com.WKNS.gather.helperMethods.DateFormatter;
import com.WKNS.gather.helperMethods.DownloadImageTask;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.EventViewHolder;
import com.WKNS.gather.databaseModels.Users.UserEvent;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_events, parent, false);
        return new EventViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        UserEvent e = mEvents.get(position);

        String imgURI = e.getProfileImg();

        if (imgURI == null || imgURI.isEmpty()) {
            holder.mImageView.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
        } else {
            new DownloadImageTask(holder.mImageView).execute(imgURI);
        }

        // Host name formatting
        StringBuilder b = new StringBuilder("Hosted by:\n");
        b.append(e.getOwnerFirstName());
        b.append(" ");
        b.append(e.getOwnerLastName());

        holder.mTextViewTitle.setText(e.getTitle());
        holder.mTextViewHost.setText(b.toString());
        holder.mTextViewDate.setText(DateFormatter.getFormattedDate(e.getDate()));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}

