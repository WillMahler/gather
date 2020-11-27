package com.WKNS.gather.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.R;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageView;
    public TextView mTextViewTitle, mTextViewHost, mTextViewDate;

    public EventViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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