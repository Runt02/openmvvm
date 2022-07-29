package com.runt.open.mvvm.ui.main.home;

import com.runt.open.mvvm.base.model.LoadPageViewModel;
import com.runt.open.mvvm.data.Results;

public class HomeViewModel extends LoadPageViewModel<Results.MessageResult> {

    @Override
    protected String requestUrl() {
        return "getMsgList";
    }
}