package com.runt.open.mvvm.base.model;

import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.PageResult;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Map;

/**
 * 分页
 * Created by Administrator on 2021/11/3 0003.
 */
public abstract class LoadPageViewModel<D> extends BaseViewModel {

    public final int SIZE = 10;
    private MutableLiveData<ArrayList<D>> liveData = new MutableLiveData<>();
    private MutableLiveData liveFailed = new MutableLiveData();

    /**
     * 请求地址
     * @return
     */
    protected abstract String requestUrl();

    /**
     * 数据请求
     * @param page  页数
     * @param param 请求参数
     */
    public void requestData(int page, Map param){
        final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<D> entityClass = (Class<D>) type.getActualTypeArguments()[0];
        httpObserverOn( commonApi.getPageData(requestUrl(), page, SIZE, param), new HttpObserver<PageResult>() {
            @Override
            protected void onSuccess(PageResult data) {
                //数据转换
                ArrayList<D> list = new ArrayList<>();
                for(Object map : data.rows){
                    list.add(new Gson().fromJson(new JSONObject((Map) map).toString(),entityClass));
                }
                liveData.postValue(list);
            }

            @Override
            protected void onFailed(HttpApiResult httpResult) {
                mActivity.showToast(httpResult.msg);
                liveFailed.postValue(1);
            }
        });
    }
    public MutableLiveData<ArrayList<D>> getLiveData(){
        return liveData;
    }

    public MutableLiveData getLiveFailed() {
        return liveFailed;
    }

}
