package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.WKNS.gather.R;
import com.WKNS.gather.databaseModels.Events.Attendee;
import com.WKNS.gather.databaseModels.Events.Invitation;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.UserViewHolder;

import java.util.ArrayList;

public class InvitationRecyclerViewAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private ArrayList<Invitation> mInvitees;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public InvitationRecyclerViewAdapter(ArrayList<Invitation> data) {
        mInvitees = data;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_search_people, parent, false);
        return new UserViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Invitation u = mInvitees.get(position);

        holder.mImageView.setImageResource(R.drawable.ic_baseline_person_24);
        holder.mTextViewName.setText(u.getFirstName() + "\n" + u.getLastName());
    }

    @Override
    public int getItemCount() {
        return mInvitees.size();
    }
}