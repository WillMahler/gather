package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.helperMethods.DateFormatter;
import com.WKNS.gather.recyclerViews.clickListeners.OnInviteClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.InviteViewHolder;
import com.WKNS.gather.testData.Notification;
import com.WKNS.gather.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InviteRecyclerViewAdapter extends RecyclerView.Adapter<InviteViewHolder> {

    private ArrayList<Notification> mNotifications;
    private OnInviteClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnInviteClickListener listener) {
        mOnItemClickListener = listener;
    }

    public InviteRecyclerViewAdapter(ArrayList<Notification> notifications) {
        mNotifications = notifications;
    }

    @NonNull
    @Override
    public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_notification, parent, false);
        return new InviteViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder holder, int position) {
        Notification n = mNotifications.get(position);

        Notification.Type type = n.getType();

        if (type == Notification.Type.EVENT_INVITE) {
            holder.mImageView.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
            holder.mTextViewTitle.setText(R.string.notification_title_event);
            holder.mTextViewContent.setText(n.getmEventTitle() + "\n" +
                    "Hosted by: " + n.getmHostName() + "\n" +
                     "Date: " + DateFormatter.getFormattedDate(n.getmTime()));

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
