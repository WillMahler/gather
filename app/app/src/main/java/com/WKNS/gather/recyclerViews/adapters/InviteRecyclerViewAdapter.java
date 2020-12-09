package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.WKNS.gather.helperMethods.DateFormatter;
import com.WKNS.gather.helperMethods.DownloadImageTask;
import com.WKNS.gather.databaseModels.Users.UserEvent;
import com.WKNS.gather.recyclerViews.clickListeners.OnInviteClickListener;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.InviteViewHolder;
import com.WKNS.gather.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InviteRecyclerViewAdapter extends RecyclerView.Adapter<InviteViewHolder> {

    private ArrayList<UserEvent> mNotifications;
    private OnInviteClickListener mOnItemClickListener;

    private OnItemClickListener mOnCardClickListener;

    public void setCardClickListener(OnItemClickListener listener) {
        mOnCardClickListener = listener;
    }

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
        return new InviteViewHolder(v, mOnItemClickListener, mOnCardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder holder, int position) {
        UserEvent n = mNotifications.get(position);

        String imgURI = n.getProfileImg();

        if (imgURI == null || imgURI.isEmpty()) {
            holder.mImageView.setImageResource(R.drawable.ic_testimg_6_ft_apart_24);
        } else {
            new DownloadImageTask(holder.mImageView).execute(imgURI);
        }

        holder.mTextViewTitle.setText(R.string.notification_title_event);
        holder.mTextViewEventTitle.setText(n.getTitle());

        // Host name formatting
        StringBuilder b = new StringBuilder("Hosted by:\n");
        b.append(n.getOwnerFirstName());
        b.append(" ");
        b.append(n.getOwnerLastName());

        holder.mTextViewHost.setText(b.toString());
        holder.mTextViewEventDate.setText(DateFormatter.getFormattedDate(n.getDate()));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }
}
