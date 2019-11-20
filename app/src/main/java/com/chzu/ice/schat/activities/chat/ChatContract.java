package com.chzu.ice.schat.activities.chat;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;

public interface ChatContract {
    public interface View extends BaseView<Presenter> {
    }

    public interface Presenter extends BasePresenter {
        void sendMessage(String content);

        void receiveMessage(String content);
    }
}
