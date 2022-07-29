package com.runt.open.mvvm.base.model;

import androidx.lifecycle.MutableLiveData;

import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.PageResult;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;

import java.util.List;
import java.util.Map;

/**
 * 分页
 * Created by Administrator on 2021/11/3 0003.
 */
public abstract class LoadPageViewModel<RESULT extends PageResult> extends BaseViewModel {

    public final int SIZE = 10;
    private MutableLiveData<List> liveData = new MutableLiveData<>();
    private MutableLiveData liveFailed = new MutableLiveData();

    protected abstract String requestUrl();

    public void requestData(int page,Map param){
        httpObserverOn( commonApi.getPageData(requestUrl(), page, SIZE, param), new HttpObserver<RESULT>() {

            @Override
            protected void onSuccess(RESULT data) {
                liveData.postValue(data.rows);
            }

            @Override
            protected void onFailed(HttpApiResult httpResult) {
                mActivity.showToast(httpResult.msg);
                liveFailed.postValue(1);
            }
        });
    }

    public MutableLiveData<List> getLiveData(){
        return liveData;
    }

    public MutableLiveData getLiveFailed() {
        return liveFailed;
    }
}
