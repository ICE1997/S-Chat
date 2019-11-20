package com.chzu.ice.schat.activities.main.chatsList;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.schat.R;
import com.chzu.ice.schat.activities.chat.ChatActivity;
import com.chzu.ice.schat.adpters.ChatListRCVAdapter;
import com.chzu.ice.schat.views.custom.CustomLinearLayoutManager;

public class ChatsListFragment extends Fragment implements ChatsListContract.View {
    private static final String TAG = ChatsListFragment.class.getSimpleName();
    private RecyclerView chatListRv;
    private TextView selectTV;
    private TextView readTV;
    private TextView deleteTV;
    private ViewGroup bottomNav;
    private View bottomNavNormal;
    private View bottomNavEdit;
    private TextView editStateChangeBtn;
    private ChatListRCVAdapter chatRCVAdapter;
    private boolean isEditMode = false;


    public ChatsListFragment() {

    }

    public static ChatsListFragment newInstance() {
        return new ChatsListFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null) {
            editStateChangeBtn = fragmentActivity.findViewById(R.id.editStateChangeBtn);
            bottomNav = fragmentActivity.findViewById(R.id.bottomNav);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag_chatslist, container, false);
        chatListRv = root.findViewById(R.id.chatListRV);
        new Handler().post(() -> {
            preInflateView();
            chatListInit();
            registerListeners();
        });
        return root;
    }

    @Override
    public void setPresenter(ChatsListContract.Presenter presenter) {

    }

    private void registerListeners() {
        editStateChangeBtn.setOnClickListener(new OnChangeEditModeListener());

        //选择单个item的监听器
        chatRCVAdapter.setOnSelectListener((v, p) -> {
            Log.d(TAG, "onSelected:" + p + "Selected !");
            if (isEditMode) {
                if (chatRCVAdapter.isSelected(p)) {
                    chatRCVAdapter.select(p, false);
                } else {
                    chatRCVAdapter.select(p, true);
                }
                if (!chatRCVAdapter.isSelectedItemHasUnReadItem()) {
                    readTV.setEnabled(false);
                } else {
                    readTV.setEnabled(true);
                }

                if (!chatRCVAdapter.isSelectedItemBoxEmpty()) {
                    deleteTV.setEnabled(true);
                } else {
                    deleteTV.setEnabled(false);
                }
            }
        });

        chatRCVAdapter.setOnClickListener((v, p) -> {
            if (!isEditMode) {
                goToChat();
                if (chatRCVAdapter.isRead(p)) {
                    Log.d(TAG, "onClick: 消息已读" + p);
                } else {
                    chatRCVAdapter.read(p, true);
                }
            }
        });
    }

    private void goToChat() {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        startActivity(intent);
    }

    private void preInflateView() {
        new Handler().post(() -> {
            bottomNavEdit = View.inflate(getContext(), R.layout.base_bottom_nav_edit, null);
            selectTV = bottomNavEdit.findViewById(R.id.tvSelectAll);
            readTV = bottomNavEdit.findViewById(R.id.tvRead);
            deleteTV = bottomNavEdit.findViewById(R.id.tvDelete);
        });
    }

    private void chatListInit() {
        CustomLinearLayoutManager llm = new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        llm.setItemPrefetchEnabled(true);
        llm.setInitialPrefetchItemCount(5);
        chatListRv.setLayoutManager(llm);
        chatListRv.setHasFixedSize(true);
        chatListRv.getItemAnimator().setChangeDuration(0);
        chatRCVAdapter = new ChatListRCVAdapter();

        chatRCVAdapter.setListController(new ListController());

        chatListRv.setAdapter(chatRCVAdapter);
    }


    private final class OnChangeEditModeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            System.out.println("DON'T click me!");
            //普通模式跳转到编辑模式
            if (!isEditMode) {
                isEditMode = true;
                final TextView selectAllTV = bottomNavEdit.findViewById(R.id.tvSelectAll);
                final TextView readTV = bottomNavEdit.findViewById(R.id.tvRead);
                final TextView deleteTV = bottomNavEdit.findViewById(R.id.tvDelete);
                editStateChangeBtn.setText("Done");
                bottomNavNormal = bottomNav.getChildAt(0);
                bottomNav.removeViewAt(0);
                bottomNav.addView(bottomNavEdit);

                Log.i(TAG, "onClick: " + bottomNav.getHeight() + "\t" + bottomNavEdit.getMinimumHeight());
                animateHeight(bottomNav, bottomNav.getHeight(), bottomNavEdit.getMinimumHeight());

                readTV.setOnClickListener(v12 -> chatRCVAdapter.readFromSelectedItems());

                selectAllTV.setOnClickListener(v13 -> {
                    if (!chatRCVAdapter.hasSelectedAll()) {
                        chatRCVAdapter.selectAll(true);
                    } else {
                        chatRCVAdapter.selectAll(false);
                    }
                });
                deleteTV.setOnClickListener(v1 -> chatRCVAdapter.deleteFromSelectedItems());

            } else {
                //编辑模式跳转到普通模式
                editStateChangeBtn.setText("Edit");
                isEditMode = false;
                bottomNavEdit = bottomNav.getChildAt(0);
                bottomNav.removeViewAt(0);
                bottomNav.addView(bottomNavNormal);
                Log.i(TAG, "onClick: " + bottomNavEdit.getHeight() + "\t" + bottomNavNormal.getHeight());
                animateHeight(bottomNav, bottomNavEdit.getHeight(), bottomNavNormal.getHeight());
            }
            chatRCVAdapter.setEditMode(isEditMode);
        }

        private void animateHeight(View view, int from, int to) {
            ValueAnimator valueAnimator = new ValueAnimator();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            valueAnimator.setIntValues(from, to);
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(animation -> {
                layoutParams.height = (int) (Integer) animation.getAnimatedValue();
                view.setLayoutParams(layoutParams);
                view.requestLayout();
            });
            valueAnimator.start();
        }
    }

    public class ListController implements ChatListRCVAdapter.ListController {
        @Override
        public void updateSelectState(int state) {
            if (state == ChatListRCVAdapter.STATE_HAS_SELECTED_ALL) {
                Log.i(TAG, "updateSelectState: SELECTED ALL");
                selectTV.setEnabled(true);
                selectTV.setText("Selected All");
            } else if (state == ChatListRCVAdapter.STATE_NOTING_TO_SELECT) {
                Log.i(TAG, "updateSelectState: Noting TO BE SELECTED");
                selectTV.setEnabled(false);
                selectTV.setText("Select All");
            } else {
                selectTV.setEnabled(true);
                selectTV.setText("Select All");
            }
        }

        @Override
        public void updateReadState(int state) {
            if (state == ChatListRCVAdapter.STATE_NOTING_TO_READ) {
                readTV.setEnabled(false);
            } else {
                readTV.setEnabled(true);
            }
        }

        @Override
        public void updateDeleteState(int state) {
            if (state == ChatListRCVAdapter.STATE_NOTING_TO_DELETE) {
                deleteTV.setEnabled(false);
            } else {
                deleteTV.setEnabled(true);
            }
        }
    }
}
