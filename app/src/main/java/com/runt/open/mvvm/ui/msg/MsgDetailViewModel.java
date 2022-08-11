package com.runt.open.mvvm.ui.msg;

import androidx.lifecycle.MutableLiveData;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-29.
 */
public class MsgDetailViewModel extends BaseViewModel {

    MutableLiveData<Results.Message> detailLive = new MutableLiveData<>();

    public void getMsgDetail(String id){
        httpObserverOn(commonApi.getMsgDetail(id), new HttpObserver<Results.Message>() {
            @Override
            protected void onSuccess(Results.Message data) {
                if(mActivity.isNull(data)){
                    mActivity.showToast("消息已被删除");
                    mActivity.finish();
                }else{
                    detailLive.setValue(data);
                }

            }

            @Override
            protected void onFailed(HttpApiResult error) {
                super.onFailed(error);
                mActivity.finish();
            }
        });
    }

}
