package com.runt.open.mvvm.base.model;

/**
 * 分页
 * Created by Administrator on 2021/11/3 0003.
 */
public abstract class BaseLoadPageViewModel extends BaseViewModel {

    public abstract void onRefresh();

    public abstract void onLoadMore();

}
