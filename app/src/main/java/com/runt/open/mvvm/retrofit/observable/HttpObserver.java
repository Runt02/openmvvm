package com.runt.open.mvvm.retrofit.observable;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.runt.open.mvvm.data.BaseApiResult;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.SocketTimeoutException;

import io.reactivex.SingleObserver;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

/**
 * 网络请求观察
 * Created by Administrator on 2021/11/11 0011.
 */
public abstract class HttpObserver<M extends BaseApiResult> extends DisposableObserver<Response<M>> implements SingleObserver<Response<M>> {

    final String TAG = "HttpObserver";

    MutableLiveData<M> resultLive;

    public HttpObserver(MutableLiveData<M> resultLive) {
        this.resultLive = resultLive;
    }


    @Override
    public void onNext(Response<M> response) {
        onExcuted(response);
    }

    @Override
    public void onError(Throwable throwable) {
        Log.i("subscribe","onError");

        try {
            Log.e(TAG,this.getClass().getSimpleName()+" "+throwable.getMessage());
            Class<M> entityClass = (Class<M>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            M t = entityClass.newInstance();//实例化一个泛型
            t.code = 410;
            if( throwable instanceof SocketTimeoutException){
                t.msg = "服务请求超时，请稍候再试";//设置错误信息
            }else{
                t.msg = "网络连接不畅，请检查您的网络设置";//设置错误信息
            }
            resultLive.setValue(t);
        } catch (ClassCastException e) {
            e.printStackTrace();
            M t = (M) new BaseApiResult<String>();
            t.code = 409;
            t.msg = "实例化对象未指定泛型实体类";
            resultLive.setValue(t);
        } catch (Exception e) {
            e.printStackTrace();
            M t = (M) new BaseApiResult<String>();
            t.code = 409;
            t.msg = e.getMessage();
            resultLive.setValue(t);
        }
    }

    @Override
    public void onSuccess(Response<M> response) {
        onExcuted(response);
    }

    private void onExcuted(@NonNull Response<M> response){

        if(response.body() != null){
            resultLive.setValue(response.body());
        }else{
            try {
                String error = response.errorBody().string();
                Log.i("subscribe","onExcuted "+error);
                onError(new Throwable(error));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onComplete() {
        Log.i("subscribe","onComplete");
    }

}
