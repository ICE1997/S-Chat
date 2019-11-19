package com.chzu.ice.schat.adpters;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chzu.ice.schat.R;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRCVAdapter extends RecyclerView.Adapter<ChatRCVAdapter.ViewHolder> {
    public static final int STATE_NOTING_TO_READ = -1;
    public static final int STATE_NOTING_TO_DELETE = -2;
    public static final int STATE_NOTING_TO_SELECT = -3;
    public static final int STATE_HAS_SELECTED_ALL = 0;

    private static final String TAG = ChatRCVAdapter.class.getSimpleName();
    private final ArrayList<ChatListItem> chatListItemStates = new ArrayList<>();
    private final LinkedHashSet<Integer> selectedItemBox = new LinkedHashSet<>();
    private final LinkedHashSet<Integer> unSelectedItemBox = new LinkedHashSet<>();
    private final LinkedHashSet<Integer> unReadItemBox = new LinkedHashSet<>();
    private boolean hasSelectedAll;
    private boolean isEditMode = false;
    private OnSelectListener onSelectListener;
    private OnClickListener onClickListener;
    private ListController listController;


    public ChatRCVAdapter() {
        selectedItemBox.clear();
        unReadItemBox.clear();
        hasSelectedAll = false;
        this.setHasStableIds(true);

        for (int i = 0; i < 20; i++) {
            unReadItemBox.add(i);
            unSelectedItemBox.add(i);
            chatListItemStates.add(new ChatListItem(i, false, false, i, String.valueOf(i), "hi,你好", "12:00"));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_chat_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ChatListItem chatListItemState = chatListItemStates.get(holder.getAdapterPosition());

        holder.senderName.setText(chatListItemState.getMessageSender());

        new Handler().post(() -> Glide.with(holder.itemView).load("https://img2.woyaogexing.com/2019/11/16/d1f0f2a6e50245eaa26fbe47e9130638!400x400.jpeg").into(holder.avatar));

        if (!chatListItemState.isRead()) {
            Log.d(TAG, "onBindViewHolder: Item" + position + "为未读!");
            holder.newMsgNum.setText(String.valueOf(chatListItemState.getNewMessageNumber()));
            holder.newMsgNum.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onBindViewHolder: Item" + position + "为已读!");
            holder.newMsgNum.setVisibility(View.GONE);
        }

        holder.latestReceiveTime.setText(chatListItemState.getReceivedTime());
        holder.latestMsg.setText(chatListItemState.getLatestMessage());

        /*点击事件，分为两种
         *(1)编辑模式，用于选择
         *(2)非编辑模式，用于跳转到聊天界面
         */
        holder.itemView.setOnClickListener(v -> {
            if (isEditMode) {
                if (onSelectListener != null) {
                    onSelectListener.onSelected(v, position);
                } else {
                    Log.e(TAG, "onClick: onSelectListener 为NULL");
                }
            } else {
                if (onClickListener != null) {
                    onClickListener.onClick(v, position);
                } else {
                    Log.e(TAG, "onClick: onClickListener 为NULL");
                }
            }
        });


        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.rbSelect.getLayoutParams();

        if (isEditMode) {
            holder.rbSelect.setVisibility(View.VISIBLE);
            if (isSelected(position)) {
                holder.rbSelect.setChecked(true);
            } else {
                holder.rbSelect.setChecked(false);
            }
            layoutParams.width = 96;
            layoutParams.height = 96;
            layoutParams.setMarginStart(32);
        } else {
            holder.rbSelect.setVisibility(View.GONE);
            layoutParams.setMarginStart(0);
            layoutParams.width = 0;
            layoutParams.height = 0;
        }
        holder.rbSelect.setLayoutParams(layoutParams);
        holder.rbSelect.requestLayout();
        Log.d(TAG, "onBindViewHolder: Updated:" + position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatListItemStates.size();
    }

    public boolean isChatListStateEmpty() {
        return chatListItemStates.size() <= 0;
    }

    //判断选择的项目中是否存在未读的item
    public boolean isSelectedItemHasUnReadItem() {
        for (Integer i : selectedItemBox) {
            if (unReadItemBox.contains(i)) {
                return true;
            }
        }
        return false;
    }

    //用于判断是否选择item
    public boolean isSelectedItemBoxEmpty() {
        return selectedItemBox.isEmpty();
    }

    public boolean isUnreadItemBoxEmpty() {
        return unReadItemBox.isEmpty();
    }

    public void setEditMode(Boolean isEditMode) {
        this.isEditMode = isEditMode;
        if (!isEditMode) {
            unSelectedItemBox.addAll(selectedItemBox);
            selectedItemBox.clear();
        }
        notifyDataSetChanged();
        updateSelectState();
    }

    public void select(int p, boolean select) {
        //TODO：更新好友聊天列表状态数据库
        if (select) {
            selectedItemBox.add(p);
            unSelectedItemBox.remove(p);
        } else {
            selectedItemBox.remove(p);
            unSelectedItemBox.add(p);
        }

        notifyItemChanged(p, null);
        updateSelectState();
        updateReadState();
        updateDeleteState();
    }

    public void selectAll(boolean select) {
        for (ChatListItem c :
                chatListItemStates) {
            int p = chatListItemStates.indexOf(c);
            select(p, select);
        }
    }

    public boolean hasSelectedAll() {
        return this.hasSelectedAll;
    }

    private void updateSelectState() {
        if (isEditMode) {
            if (chatListItemStates.isEmpty()) {
                this.hasSelectedAll = false;
                listController.updateSelectState(STATE_NOTING_TO_SELECT);
            } else if (unSelectedItemBox.isEmpty()) {
                Log.i(TAG, "updateSelectState: 已选择全部");
                this.hasSelectedAll = true;
                listController.updateSelectState(STATE_HAS_SELECTED_ALL);
            } else {
                Log.i(TAG, "updateSelectState: 还未选择全部 ");
                this.hasSelectedAll = false;
                listController.updateSelectState(1);
            }
        }
    }

    private void updateReadState() {
        if (isEditMode) {
            if (isSelectedItemHasUnReadItem()) {
                listController.updateReadState(1);
            } else {
                listController.updateReadState(STATE_NOTING_TO_READ);
            }
        }
    }

    private void updateDeleteState() {
        if (isEditMode) {
            if (isSelectedItemBoxEmpty()) {
                listController.updateDeleteState(STATE_NOTING_TO_DELETE);
            } else {
                listController.updateDeleteState(1);
            }
        }
    }

    public void read(int p, boolean read) {
        ChatListItem chatListItemState = chatListItemStates.get(p);
        chatListItemState.setRead(read);
        chatListItemStates.set(p, chatListItemState);
        if (read) {
            Log.d(TAG, "onBindViewHolder: Item" + p + "被设置为已读!");
            unReadItemBox.remove(p);
        } else {
            Log.d(TAG, "onBindViewHolder: Item" + p + "被设置为未读!");
            unReadItemBox.add(p);
        }
        notifyItemChanged(p);
        updateSelectState();
        updateReadState();
        updateDeleteState();
    }

    public void readFromSelectedItems() {
        for (Integer i : selectedItemBox) {
            read(i, true);
            unSelectedItemBox.add(i);
        }
        selectedItemBox.clear();
    }

    /*从已选的items中删除
     */
    public void deleteFromSelectedItems() {
        ArrayList<ChatListItem> temp = new ArrayList<>();
        for (Integer i : selectedItemBox) {
            Log.d(TAG, "deleteFromSelectedItems: " + i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, chatListItemStates.size() - i);
            temp.add(chatListItemStates.get(i));
            unSelectedItemBox.remove(i);
        }
        chatListItemStates.removeAll(temp);

        unSelectedItemBox.clear();
        unReadItemBox.clear();
        selectedItemBox.clear();
        for (int i = 0; i < chatListItemStates.size(); i++) {
            unSelectedItemBox.add(i);
            if (!chatListItemStates.get(i).isRead()) {
                unReadItemBox.add(i);
            }
        }

        updateSelectState();
        updateReadState();
        updateDeleteState();
    }

    /**
     * 根据item的位置来判断这个item是否已经是已读。通过增加一个未读数组即可。
     *
     * @param p item的位置
     * @return 如果unReadItemBox含有这个item, 则就返回true, 否则返回false
     */
    public boolean isRead(int p) {
        return !unReadItemBox.contains(p);
    }


    public boolean isSelected(int p) {
        return selectedItemBox.contains(p);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    /**
     * 聊天列表的选择全都，读，删的操作
     */
    public void setListController(ListController listController) {
        this.listController = listController;
    }

    public interface OnSelectListener {
        void onSelected(View v, int p);
    }


    public interface OnClickListener {
        void onClick(View v, int p);
    }

    //用接口将RecycleListView的控制器（下方的按钮）联系在一起
    public interface ListController {
        void updateSelectState(int state);

        void updateReadState(int state);

        void updateDeleteState(int state);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final RadioButton rbSelect;
        private final TextView senderName;
        private final TextView latestMsg;
        private final TextView latestReceiveTime;
        private final TextView newMsgNum;
        private final CircleImageView avatar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rbSelect = itemView.findViewById(R.id.rbSelect);
            senderName = itemView.findViewById(R.id.tvName);
            latestMsg = itemView.findViewById(R.id.tvLatestMsg);
            latestReceiveTime = itemView.findViewById(R.id.tvReceiveTime);
            newMsgNum = itemView.findViewById(R.id.tvNewMsgNum);
            avatar = itemView.findViewById(R.id.imAvatar);
        }
    }
}
