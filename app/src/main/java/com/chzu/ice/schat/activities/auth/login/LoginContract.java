package com.chzu.ice.schat.activities.auth.login;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showLoginFailedForWrongUsernameOrPassword();

        void showLoginSucceed();

        void afterLogin();

        void endLogin();
    }

    interface Presenter extends BasePresenter {
        void login(String username, String password);
    }
}
