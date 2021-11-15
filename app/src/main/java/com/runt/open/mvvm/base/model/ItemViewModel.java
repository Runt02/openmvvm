package com.runt.open.mvvm.base.model;

import androidx.lifecycle.MutableLiveData;

/**
 * Created by Administrator on 2021/11/5 0005.
 */
public class ItemViewModel<T> extends BaseViewModel {

    MutableLiveData<T> liveData = new MutableLiveData<>();

    public MutableLiveData<T> getLiveData() {
        return liveData;
    }
}
