package com.runt.open.mvvm.base.model;

import androidx.lifecycle.ViewModel;

import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.retrofit.AndroidScheduler;
import com.runt.open.mvvm.retrofit.api.CommonApiCenter;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.retrofit.utils.RetrofitUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2021/11/11 0011.
 */
public class BaseViewModel extends ViewModel {

    protected BaseActivity mActivity;
    protected CommonApiCenter commonApi = RetrofitUtils.getInstance().getCommonApi();

    public void onCreate(BaseActivity activity) {
        this.mActivity = activity;
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
                    mActivity.showLoadingDialog("");
                })
                .observeOn(AndroidScheduler.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
                .doOnComplete(() -> {
                    mActivity.dissmissLoadingDialog();
                })
                .subscribe(observer);
    }



}
