package com.runt.open.mvvm.ui.coin;

import android.content.Intent;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.ui.login.UserBean;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2019-3-26.
 */
public class CoinViewModel extends BaseViewModel {

    //更新真实姓名
    public void updateName(String name,String pass, HttpObserver<String> httpObserver){
        httpObserverOnLoading(commonApi.updateRealname(name,pass),httpObserver);
    }

    //更新支付宝
    public void updateAlipay(String alipay,String pass, HttpObserver<String> httpObserver){
        httpObserverOnLoading(commonApi.updateAlipay(alipay,pass),httpObserver);
    }

    //提现
    public void withDraw(String pass,int count){
        httpObserverOnLoading(commonApi.withDraw(pass, count), new HttpObserver<String>(mActivity) {
            @Override
            protected void onSuccess(String data) {
                UserBean.getUser().setCoin(UserBean.getUser().getCoin()-count);
                mActivity.showToast("申请成功");
                mActivity.setResult(mActivity.RESULT_OK);
                mActivity.finish();
            }

            @Override
            protected void onFailed(HttpApiResult error) {
                if(error.code == 40004){
                    mActivity.showDialog("申请失败", "未找到对应的支付宝账户", "设置", "取消", new ResPonse() {
                        @Override
                        public void doSuccess(Object obj) {
                            mActivity.startActivity(new Intent(mActivity, CoinSettingActivity.class) );//去设置密码
                        }
                    });
                }else if(error.code == 622){
                    mActivity.showDialog("申请失败",error.msg,null);
                }else{
                    super.onFailed(error);
                }
            }
        });
    }
}
