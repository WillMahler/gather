package com.WKNS.gather.ui.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.testData.Notification;
import com.WKNS.gather.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationViewHolder> {

    private ArrayList<Notification> mNotifications;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onAcceptClick(int position);
        void onDeclineClick(int position);
    }

    public void setmOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewTitle, mTextViewContent;
        public Button mButtonAccept, mButtonDecline;

        public NotificationViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageview_friend_or_event);
            mTextViewTitle = itemView.findViewById(R.id.textview_title);
            mTextViewContent = itemView.findViewById(R.id.textview_content);
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

    public NotificationRecyclerViewAdapter(ArrayList<Notification> notifications) {
        mNotifications = notifications;
    }

    @NonNull
    @Override
    public NotificationRecyclerViewAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_notification, parent, false);
        return new NotificationViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification n = mNotifications.get(position);

        Notification.Type type = n.getType();

        if (type == Notification.Type.EVENT_INVITE) {
            holder.mImageView.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
            holder.mTextViewTitle.setText(R.string.notification_title_event);
            holder.mTextViewContent.setText(n.getmEventTitle() + "\n" +
                    "Hosted by: " + n.getmHostName() + "\n" +
                     "Date: " + n.getmTime().toString().substring(0, 23));

        } else if (type == Notification.Type.FRIEND_REQUEST) {
            holder.mImageView.setImageResource(R.drawable.ic_baseline_person_add_24);
            holder.mTextViewTitle.setText(R.string.notification_title_friend);
            holder.mTextViewContent.setText(n.getmRequesterFirstName() + " " + n.getmRequesterLastName());

        }
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }
}
