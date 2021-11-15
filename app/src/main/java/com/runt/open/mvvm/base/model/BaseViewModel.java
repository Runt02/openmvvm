package com.runt.open.mvvm.base.model;

import androidx.lifecycle.ViewModel;

import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.retrofit.AndroidScheduler;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2021/11/11 0011.
 */
public class BaseViewModel extends ViewModel {


    /**
     * 网络请求观察
     * @param observable
     * @param <T>
     * @return
     */
    public <T> void httpObserverOn(Observable<T> observable, HttpObserver observer){
        observable.subscribeOn(Schedulers.io())//指定网络请求在io后台线程中进行
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(observer);
    }



}
