package com.chzu.ice.schat.activities;

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
