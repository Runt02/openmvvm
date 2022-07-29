package com.runt.open.mvvm.retrofit.observable;

import android.util.Log;

import com.google.gson.Gson;
import com.runt.open.mvvm.data.HttpApiResult;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * 网络请求观察
 * Created by Administrator on 2021/11/11 0011.
 */
public abstract class HttpObserver<RESULT> implements Observer<HttpApiResult<RESULT>> {

    final String TAG = "HttpObserver";

    @Override
    public void onSubscribe(Disposable d) {
        Log.d(TAG,"onSubscribe "+hashCode());
    }

    @Override
    public void onNext(HttpApiResult<RESULT> httpResult) {
        Log.d(TAG,"onNext "+httpResult);
        if (httpResult != null && httpResult.code == 0) {
            onSuccess(httpResult.data);
        }else{
            onFailed(httpResult);//接口返回错误
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e(TAG,"onError "+e.getMessage()+" "+hashCode());
        int code = 600;
        String msg = "网络请求失败，请检查网络或稍后重试";
        if( e instanceof ConnectException || e instanceof TimeoutException
                || e instanceof SocketTimeoutException || e instanceof UnknownHostException){
            code = 601;
            msg = "网络请求失败，请检查网络或稍后重试";
        }else if( e instanceof HttpException){
            String regEx = "[^0-9]";
            Log.i(TAG,"code:"+ Pattern.compile(regEx).matcher(e.getMessage()).replaceAll(""));
            String error = Pattern.compile(regEx).matcher(e.getMessage()).replaceAll("");
            code = error.length()>0?Integer.parseInt(error):500;
            msg = error.length()>0?"服务器请求失败":"登录信息验证失败";
        }
        HttpApiResult httpResult = new Gson().fromJson("{'code':"+code+",'msg':'"+msg+"'}", HttpApiResult.class);
        onFailed(httpResult);
    }

    @Override
    public void onComplete() {
        Log.i(TAG,"onComplete "+hashCode());
    }

    protected abstract void onSuccess(RESULT data);

    protected void onFailed(HttpApiResult httpResult){}

}
