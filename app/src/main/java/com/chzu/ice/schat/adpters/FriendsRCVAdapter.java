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

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsRCVAdapter extends RecyclerView.Adapter<FriendsRCVAdapter.ViewHolder> {
    private ArrayList<FriendListItem> friendListItems = new ArrayList<>();

    public FriendsRCVAdapter() {
        setHasStableIds(true);
        for (int i = 0; i < 100; i++) {
            friendListItems.add(new FriendListItem("https://img2.woyaogexing.com/2019/11/16/d1f0f2a6e50245eaa26fbe47e9130638!400x400.jpeg", String.valueOf(i)));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_friend_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView).load(friendListItems.get(position).getAvatarAddr()).into(holder.imAvatar);
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
