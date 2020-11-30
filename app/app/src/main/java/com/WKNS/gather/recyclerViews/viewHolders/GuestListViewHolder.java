package com.WKNS.gather.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.R;
import com.WKNS.gather.recyclerViews.clickListeners.OnRemoveClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GuestListViewHolder extends RecyclerView.ViewHolder {

    public TextView mEmailAddress;
    public ImageView mRemove;

    public GuestListViewHolder(@NonNull View itemView, final OnRemoveClickListener listener) {
        super(itemView);

        mEmailAddress = itemView.findViewById(R.id.textview_guestEmail);
        mRemove = itemView.findViewById(R.id.imageview_remove);

        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(position);
                    }
                }
            }
        });
    }
}
