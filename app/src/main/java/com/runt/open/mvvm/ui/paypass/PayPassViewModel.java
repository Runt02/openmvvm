package com.runt.open.mvvm.ui.paypass;

import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-30.
 */
public class PayPassViewModel extends BaseViewModel {

    public void updatePass(String pass,String code){
        httpObserverOnLoading(commonApi.updatePaypass(code, pass), new HttpObserver(mActivity) {
            @Override
            protected void onSuccess(Object data) {
                mActivity.finish();
                mActivity.showToast("支付密码修改成功");
            }
        });
    }

}
