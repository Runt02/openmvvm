package com.runt.open.mvvm.base.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.runt.open.mvvm.data.LoadingCmd;
import com.runt.open.mvvm.retrofit.AndroidScheduler;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2021/11/11 0011.
 */
public class BaseViewModel extends ViewModel {

    MutableLiveData<LoadingCmd> loadLive = new MutableLiveData<>();

    public MutableLiveData<LoadingCmd> getLoadLive() {
        return loadLive;
    }

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


    /**
     * 网络请求观察
     * @param observable
     * @param <T>
     * @return
     */
    public <T> void httpObserverOnLoading(Observable<T> observable, HttpObserver observer){
        observable.subscribeOn(Schedulers.io())//指定网络请求在io后台线程中进行
                .doOnSubscribe(disposable -> {
                             loadLive.setValue(new LoadingCmd(LoadingCmd.CMD.LOADING,"请求数据中..."));
                })
                .observeOn(AndroidScheduler.mainThread())
                .doOnComplete(() -> {
                    loadLive.postValue(new LoadingCmd(LoadingCmd.CMD.DISSMISS));
                })
                .subscribe(observer);
    }



}
