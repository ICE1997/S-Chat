package com.chzu.ice.schat.activities.main.settingsList;

import com.chzu.ice.schat.activities.BasePresenter;
import com.chzu.ice.schat.activities.BaseView;

public interface SettingsListConstract {
    interface View extends BaseView<Presenter> {
        @Override
        void setPresenter(Presenter presenter);
    }

    interface Presenter extends BasePresenter {

    }
}
