package com.WKNS.gather.recyclerViews.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.R;
import com.WKNS.gather.recyclerViews.clickListeners.OnInviteClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InviteViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageView;
    public TextView mTextViewTitle, mTextViewEventTitle, mTextViewHost,
                    mTextViewEventDate;
    public Button mButtonAccept, mButtonDecline;

    public InviteViewHolder(@NonNull View itemView, final OnInviteClickListener listener) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.imageview_friend_or_event);
        mTextViewTitle = itemView.findViewById(R.id.textview_invite_title);
        mTextViewEventTitle = itemView.findViewById(R.id.textview_invite_eventTitle);
        mTextViewHost = itemView.findViewById(R.id.textview_invite_eventHost);
        mTextViewEventDate = itemView.findViewById(R.id.textview_invite_eventDate);
        mButtonAccept = itemView.findViewById(R.id.button_accept);
        mButtonDecline = itemView.findViewById(R.id.button_decline);

        mButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAcceptClick(position);
                    }
                }
            }
        });

        mButtonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeclineClick(position);
                    }
                }
            }
        });
    }
}