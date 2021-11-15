package com.runt.open.mvvm.retrofit.observable;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.runt.open.mvvm.data.BaseApiResult;

import java.lang.reflect.ParameterizedType;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;

/**
 * 网络请求观察
 * Created by Administrator on 2021/11/11 0011.
 */
public abstract class HttpObserver<T extends BaseApiResult> extends DisposableObserver<T>{

    final String TAG = "HttpObserver";

    MutableLiveData<T> resultLive;

    public HttpObserver(MutableLiveData<T> resultLive) {
        this.resultLive = resultLive;
    }


    @Override
    public void onNext(T value) {
        resultLive.setValue(value);
    }

    @Override
    public void onError(Throwable throwable) {
        Log.i("subscribe","onError");

        try {
            Log.e(TAG,this.getClass().getSimpleName()+" "+throwable.getMessage());
            Class<T> entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            T t = entityClass.newInstance();//实例化一个泛型
            t.code = 410;
            if( throwable instanceof SocketTimeoutException){
                t.msg = "服务请求超时，请稍候再试";//设置错误信息
            }else{
                t.msg = "网络连接不畅，请检查您的网络设置";//设置错误信息
            }
            resultLive.setValue(t);
        } catch (ClassCastException e) {
            e.printStackTrace();
            T t = (T) new BaseApiResult<String>();
            t.code = 409;
            t.msg = "实例化对象未指定泛型实体类";
            resultLive.setValue(t);
        } catch (Exception e) {
            e.printStackTrace();
            T t = (T) new BaseApiResult<String>();
            t.code = 409;
            t.msg = e.getMessage();
            resultLive.setValue(t);
        }
    }

    @Override
    public void onComplete() {
        Log.i("subscribe","onComplete");
    }
}
