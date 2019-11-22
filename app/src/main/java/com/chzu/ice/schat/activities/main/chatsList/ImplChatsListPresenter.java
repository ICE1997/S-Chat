package com.chzu.ice.schat.activities.main.chatsList;

public class ImplChatsListPresenter implements ChatsListContract.Presenter {
    private ChatsListContract.View view;

    public ImplChatsListPresenter(ChatsListContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }
}
