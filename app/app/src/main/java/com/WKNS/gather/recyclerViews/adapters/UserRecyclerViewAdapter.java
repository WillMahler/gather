package com.WKNS.gather.recyclerViews.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.WKNS.gather.R;
import com.WKNS.gather.recyclerViews.clickListeners.OnItemClickListener;
import com.WKNS.gather.recyclerViews.viewHolders.UserViewHolder;
import com.WKNS.gather.testData.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private ArrayList<User> mUsers;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public UserRecyclerViewAdapter(ArrayList<User> data) {
        mUsers = data;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_search_people, parent, false);
        return new UserViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User u = mUsers.get(position);

        holder.mImageView.setImageResource(R.drawable.ic_baseline_person_24);
        holder.mTextViewName.setText(u.getFirstName() + "\n" + u.getLastName());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
