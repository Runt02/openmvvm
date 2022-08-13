package com.runt.open.mvvm.retrofit.Interceptor;


import com.google.gson.Gson;
import com.runt.open.mvvm.MyApplication;
import com.runt.open.mvvm.data.PhoneDevice;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.util.DeviceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * My father is Object, ites purpose of   添加header拦截器
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-10-8.
 */
public class AddHeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(addHeaders(chain.request()));
    }

    protected Request addHeaders(Request request){
        Request.Builder requestBuild = request.newBuilder()
                .addHeader("device", new Gson().toJson(PhoneDevice.getDevice()))
                .addHeader("appVersion", DeviceUtil.getAppVersionName(MyApplication.getApplication()))
                .addHeader("os", DeviceUtil.isHarmonyOS()? "harmony" : "android");
        if(UserBean.getUser() != null){
            requestBuild.addHeader("token", UserBean.getUser().getToken());
        }
        return requestBuild.build().newBuilder().build();
    }

}
