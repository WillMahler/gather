package com.WKNS.gather.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.R;
import com.WKNS.gather.testData.Event;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchRecyclerViewAdapterGatherings extends RecyclerView.Adapter<SearchRecyclerViewAdapterGatherings.SearchGatheringsViewHolder> {

    private ArrayList<Event> mEvents;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static class SearchGatheringsViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewTitle, mTextViewHost, mTextViewDate;

        public SearchGatheringsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.eventImage);
            mTextViewTitle = itemView.findViewById(R.id.eventTitle);
            mTextViewHost = itemView.findViewById(R.id.eventHost);
            mTextViewDate = itemView.findViewById(R.id.eventDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public SearchRecyclerViewAdapterGatherings(ArrayList<Event> events) {
        mEvents = events;
    }

    @NonNull
    @Override
    public SearchGatheringsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_search_gatherings, parent, false);
        return new SearchGatheringsViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchGatheringsViewHolder holder, int position) {
        Event e = mEvents.get(position);

        holder.mImageView.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
        holder.mTextViewTitle.setText(e.getTitle());
        holder.mTextViewHost.setText("Hosted by: " + e.getOwnerFirstName());
        holder.mTextViewDate.setText(e.getTime().toString().substring(0, 23));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}

