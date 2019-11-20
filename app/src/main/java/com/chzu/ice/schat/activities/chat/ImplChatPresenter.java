package com.chzu.ice.schat.activities.chat;

public class ImplChatPresenter implements ChatContract.Presenter {
    private ChatContract.View view;

    public ImplChatPresenter(ChatContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void sendMessage(String content) {

    }

    @Override
    public void receiveMessage(String content) {

    }
}
