package com.chzu.ice.schat.activities.main.chatsList;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;

public interface ChatsListContract {
    interface View extends BaseView<Presenter> {
        @Override
        void setPresenter(Presenter presenter);
    }

    interface Presenter extends BasePresenter{}
}
