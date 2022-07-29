package com.runt.open.mvvm.ui.main.mine;

import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.ui.login.UserBean;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2022/7/27.
 */
public class MineViewModel extends BaseViewModel {

    public void updateName(HttpObserver observer){
        if(UserBean.getUser().getPhone().equals(UserBean.getUser().getUsername())) {
            mActivity.showInputDialog("输入名称", UserBean.getUser().getUsername(), "名称只能修改一次", new ResPonse() {
                @Override
                public void doSuccess(Object obj) {
                    httpObserverOnLoading(commonApi.updateName(obj.toString()), observer);
                }
            });
        }
    }

    public Call<ResponseBody> updateHead(File file){
        return commonApi.updateHead(MultipartBody.Part.createFormData("head",file.getName(), RequestBody.create(MediaType.parse("text/plain"), file)));
    }

}
