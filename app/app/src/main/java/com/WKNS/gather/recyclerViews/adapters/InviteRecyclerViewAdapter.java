package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.clickListeners.OnInviteClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.InviteViewHolder;
import com.WKNS.gather.testData.Notification;
import com.WKNS.gather.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InviteRecyclerViewAdapter extends RecyclerView.Adapter<InviteViewHolder> {

    private ArrayList<UserEvent> mNotifications;
    private OnInviteClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnInviteClickListener listener) {
        mOnItemClickListener = listener;
    }

    public InviteRecyclerViewAdapter(ArrayList<UserEvent> notifications) {
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
        UserEvent n = mNotifications.get(position);

        holder.mImageView.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
        holder.mTextViewTitle.setText(R.string.notification_title_event);
        holder.mTextViewContent.setText(n.getTitle() + "\n" +
                "Hosted by: " + n.getOwnerFirstName() + "\n" +
                 "Date: " + n.getDate().toString().substring(0, 23));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }
}
