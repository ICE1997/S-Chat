package com.chzu.ice.schat.activities.auth.register;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;

public class RegisterContract {
    public interface View extends BaseView<Presenter> {
        void afterRegister();

        void endRegister();

        void showRegisterFailedForUsernameExist();

        void showRegisterSucceed();

        void showUsernameOrPasswordCantBeEmpty();
    }

    public interface Presenter extends BasePresenter {
        void register(String username, String password);
    }
}
