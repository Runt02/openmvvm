package com.runt.open.mvvm.ui.main.home;

import com.runt.open.mvvm.base.model.LoadPageViewModel;

public class HomeViewModel extends LoadPageViewModel<Message> {

    @Override
    protected String requestUrl() {
        return "getMsgList";
    }
}