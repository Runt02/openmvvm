package com.runt.open.mvvm.retrofit.utils;


import com.runt.open.mvvm.BuildConfig;
import com.runt.open.mvvm.retrofit.Interceptor.EncryptInterceptor;
import com.runt.open.mvvm.retrofit.Interceptor.HttpLoggingInterceptor;
import com.runt.open.mvvm.retrofit.api.CommonApiCenter;
import com.runt.open.mvvm.retrofit.converter.DecryptGsonConverterFactory;
import com.runt.open.mvvm.retrofit.net.NetWorkListenear;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-7-9.
 */

public class RetrofitUtils {
    static RetrofitUtils instance;
    Retrofit retrofit/*log输出，驼峰转换*/,unHumpRetrofit/*log输出，不强制驼峰转换*/,
            unLogRetrofit/*log不输出，驼峰转换*/,unLogHumpRetorfit/*log不输出，不强制驼峰转换*/;
    CommonApiCenter commonApi;//常用接口

    OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .addInterceptor(new EncryptInterceptor());
    OkHttpClient.Builder logBuilder = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor());//log打印拦截器

    public static RetrofitUtils getInstance() {
        if(instance == null){
            instance = new RetrofitUtils();
        }
        return instance;
    }

    /**
     * log输出，gson驼峰转换
     * @return
     */
    public <T> T getRetrofit(Class<T> clas) {
        if(retrofit == null){
            retrofit = getRetrofit(getOkHttpClient(logBuilder),
                    new Retrofit.Builder().addConverterFactory(DecryptGsonConverterFactory.create(true))) ;
        }
        if(!BuildConfig.DEBUG){//正式版 不打印log
            return getUnLogRetrofit(clas);
        }
        return retrofit.create(clas);
    }

    /**
     * log输出，gson不转换驼峰
     * @return
     */
    public <T> T getUnHumpRetrofit(Class<T> clas) {
        if(unHumpRetrofit == null){
            unHumpRetrofit = getRetrofit(getOkHttpClient(logBuilder),
                    new Retrofit.Builder().addConverterFactory(DecryptGsonConverterFactory.create())) ;
        }
        if(!BuildConfig.DEBUG){//正式版 不打印log
            return getUnLogHumpRetorfit(clas);
        }
        return unHumpRetrofit.create(clas);
    }

    /**
     * log不输出，gson驼峰转换
     * @return
     */
    public <T> T  getUnLogRetrofit(Class<T> clas) {
        if(unLogRetrofit == null){
            unLogRetrofit = getRetrofit(getOkHttpClient(builder),
                    new Retrofit.Builder().addConverterFactory(DecryptGsonConverterFactory.create(true))) ;
        }
        return unLogRetrofit.create(clas);
    }

    /**
     * log不输出，gson不转换驼峰
     * @return
     */
    public <T> T getUnLogHumpRetorfit(Class<T> clas) {
        if(unLogHumpRetorfit == null){
            unLogHumpRetorfit = getRetrofit(getOkHttpClient(builder),
                    new Retrofit.Builder().addConverterFactory(DecryptGsonConverterFactory.create())) ;
        }
        return unLogHumpRetorfit.create(clas);
    }

    private OkHttpClient getOkHttpClient(OkHttpClient.Builder builder){
        return builder.connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .eventListenerFactory(NetWorkListenear.get())
                .build();
    }

    private Retrofit getRetrofit(OkHttpClient client,Retrofit.Builder builder){
        return builder
                //设置OKHttpClient
                .client(client)
                //设置baseUrl,注意，baseUrl必须后缀"/"
                .baseUrl(BuildConfig.HOST_IP_ADDR+"api/v1/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    /**
     * 常用接口
     * @return
     */
    public CommonApiCenter getCommonApi(){
        if(commonApi == null){
            commonApi  = getRetrofit(CommonApiCenter.class);
        }
        return commonApi;
    }
}
