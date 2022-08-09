package com.runt.open.mvvm.ui.main.home;

import com.runt.open.mvvm.base.model.LoadPageViewModel;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.PageResult;

import io.reactivex.Observable;

public class HomeViewModel extends LoadPageViewModel<Message> {

    @Override
    public Observable<HttpApiResult<PageResult<Message>>> request(int page, Object... objects) {
        return commonApi.getMsgList(page,SIZE);
    }
}