package com.WKNS.gather.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.R;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageView;
    public TextView mTextViewName;

    public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
        super(itemView);

        mImageView = itemView.findViewById(R.id.imageview_userImage);
        mTextViewName = itemView.findViewById(R.id.textview_userName);

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