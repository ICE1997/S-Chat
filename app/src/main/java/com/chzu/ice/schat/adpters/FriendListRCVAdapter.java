package com.chzu.ice.schat.adpters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chzu.ice.schat.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListRCVAdapter extends RecyclerView.Adapter<FriendListRCVAdapter.ViewHolder> {
    private ArrayList<FriendListItem> friendListItems = new ArrayList<>();
    private OnClickListener onClickListener;

    public FriendListRCVAdapter(List<FriendListItem> friendListItems) {
        this.friendListItems = (ArrayList<FriendListItem>) friendListItems;
        setHasStableIds(true);
    }

    public FriendListRCVAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener((v -> onClickListener.onClick(friendListItems.get(position).getUsername())));

        if (friendListItems.get(position).getAvatarAddr() == null || "".equals(friendListItems.get(position).getAvatarAddr())) {
            Glide.with(holder.itemView).load("https://img2.woyaogexing.com/2019/11/16/d1f0f2a6e50245eaa26fbe47e9130638!400x400.jpeg").into(holder.imAvatar);
        }
        holder.tvName.setText(friendListItems.get(position).getUsername());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return friendListItems.size();
    }

    public void setDataSet(ArrayList<FriendListItem> friendListItems) {
        this.friendListItems = friendListItems;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(String name);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imAvatar;
        private TextView tvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imAvatar = itemView.findViewById(R.id.imAvatar);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
