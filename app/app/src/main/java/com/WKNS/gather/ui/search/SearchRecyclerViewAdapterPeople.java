package com.WKNS.gather.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.WKNS.gather.R;
import com.WKNS.gather.testData.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchRecyclerViewAdapterPeople extends RecyclerView.Adapter<SearchRecyclerViewAdapterPeople.SearchPeopleViewHolder>{

    private ArrayList<User> mUsers;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static class SearchPeopleViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewName;

        public SearchPeopleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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

    public SearchRecyclerViewAdapterPeople(ArrayList<User> data) {
        mUsers = data;
    }

    @NonNull
    @Override
    public SearchPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_search_people, parent, false);
        return new SearchPeopleViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPeopleViewHolder holder, int position) {
        User u = mUsers.get(position);

        holder.mImageView.setImageResource(R.drawable.ic_baseline_person_24);
        holder.mTextViewName.setText(u.getFirstName() + "\n" + u.getLastName());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
