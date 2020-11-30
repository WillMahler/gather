package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.WKNS.gather.R;
import com.WKNS.gather.recyclerViews.clickListeners.OnRemoveClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.GuestListViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GuestListRecyclerViewAdapter extends RecyclerView.Adapter<GuestListViewHolder> {

    private ArrayList<String> mGuestEmails;
    private OnRemoveClickListener mOnRemoveClickListener;

    public void setOnItemClickListener(OnRemoveClickListener listener) {
        mOnRemoveClickListener = listener;
    }

    public GuestListRecyclerViewAdapter(ArrayList<String> emails) {
        mGuestEmails = emails;
    }

    @NonNull
    @Override
    public GuestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_guestlist, parent, false);
        return new GuestListViewHolder(v, mOnRemoveClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestListViewHolder holder, int position) {
        String email = mGuestEmails.get(position);

        holder.mEmailAddress.setText(email);
    }

    @Override
    public int getItemCount() {
        return mGuestEmails.size();
    }
}


