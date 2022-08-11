package com.runt.open.mvvm.ui.coin;

import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2019-3-26.
 */
public class CoinSettingViewModel extends BaseViewModel {

    public void updateName(String name,String pass, HttpObserver<String> httpObserver){
        httpObserverOnLoading(commonApi.updateRealname(name,pass),httpObserver);
    }

    public void updateAlipay(String alipay,String pass, HttpObserver<String> httpObserver){
        httpObserverOnLoading(commonApi.updateAlipay(alipay,pass),httpObserver);
    }

}
