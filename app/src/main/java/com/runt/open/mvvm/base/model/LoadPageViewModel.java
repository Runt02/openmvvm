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
     * @param page      页数
     * @param objects   参数
     * @return
     */
    public abstract Observable<HttpApiResult<PageResult<D>>> request(int page, Object... objects);

    /**
     * 数据请求
     */
    public void requestData(Observable<HttpApiResult<PageResult<D>>> observable){
        httpObserverOn( observable, new PageHttpObserver());
    }

    public MutableLiveData<ArrayList<D>> getLiveData(){
        return liveData;
    }

    public MutableLiveData getLiveFailed() {
        return liveFailed;
    }

    public class PageHttpObserver extends HttpObserver<PageResult<D>> {
        @Override
        protected void onSuccess(PageResult<D> data) {
            liveData.postValue(data.rows);
        }

        @Override
        protected void onFailed(HttpApiResult httpResult) {
            mActivity.showToast(httpResult.msg);
            liveFailed.postValue(1);
        }
    }

}
