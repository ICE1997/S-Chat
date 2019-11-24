package com.chzu.ice.schat.adpters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.schat.R;

import java.util.ArrayList;

public class ChatRCVAdapter extends RecyclerView.Adapter<ChatRCVAdapter.ViewHolder> {
    private ArrayList<ChatItem> chatItems = new ArrayList<>();

    public ChatRCVAdapter() {
        setHasStableIds(true);
    }

    public void setChatItems(ArrayList<ChatItem> chatItems) {
        this.chatItems = chatItems;
        notifyDataSetChanged();
    }

    public void addChatItem(ChatItem chatItem) {
        this.chatItems.add(chatItem);
        notifyItemRangeInserted(chatItems.size() - 1, 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.msgTV.setText(chatItems.get(position).getContent());
        holder.timeTV.setText(chatItems.get(position).getTime());
        if (chatItems.get(position).isSender()) {
            ((LinearLayout) holder.itemView).setGravity(Gravity.END);
            holder.msgTV.setTextColor(Color.BLACK);
            holder.timeTV.setTextColor(Color.BLACK);
            holder.allDoneImg.setImageDrawable(ContextCompat.getDrawable(holder.allDoneImg.getContext(), R.drawable.ic_done_all_black_12dp));
            ((LinearLayout) holder.itemView).getChildAt(0).setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.text_view_chat_item_background_black));
        } else {
            ((LinearLayout) holder.itemView).setGravity(Gravity.START);
            ((LinearLayout) holder.itemView).getChildAt(0).setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.text_view_chat_item_background_blue));
            holder.msgTV.setTextColor(ContextCompat.getColor(holder.msgTV.getContext(), R.color.colorAccent));
            holder.timeTV.setTextColor(ContextCompat.getColor(holder.timeTV.getContext(), R.color.colorAccent));
            holder.allDoneImg.setImageDrawable(ContextCompat.getDrawable(holder.allDoneImg.getContext(), R.drawable.ic_done_all_blue_12dp));
        }
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView msgTV;
        private TextView timeTV;
        private ImageView allDoneImg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            msgTV = itemView.findViewById(R.id.msgTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            allDoneImg = itemView.findViewById(R.id.allDoneImg);
        }
    }
}
