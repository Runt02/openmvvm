package com.runt.open.mvvm.base.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.base.adapter.BaseAdapter;
import com.runt.open.mvvm.base.model.BaseLoadPageViewModel;
import com.runt.open.mvvm.data.BasePageResult;
import com.runt.open.mvvm.databinding.RefreshRecyclerBinding;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

/**
 * 分页fragment 封装
 * Created by Administrator on 2021/11/3 0003.
 */
public abstract class BaseLoadPageFragment<B extends ViewBinding,VM extends BaseLoadPageViewModel,A extends BaseAdapter,D extends BasePageResult> extends BaseFragment<B,VM>
        implements OnRefreshLoadMoreListener {
    protected SmartRefreshLayout smartRefresh;
    protected RecyclerView recycler;
    final String TAG = "RecyclerFragment";
    protected A adapter;//适配器
    protected String url;//请求地址
    protected HashMap param;//参数
    protected int page=1;
    protected final int SIZE = 10;
    protected boolean isRefresh = false;//是否正在刷新

    @Override
    public void initViews() {
        try {
            Class<A> entityClass = (Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
            this.adapter = entityClass.newInstance();//实例化泛型
            String smartStr = "smartRefresh";
            smartRefresh = (SmartRefreshLayout) binding.getClass().getField(smartStr).get(binding);
            recycler =  (RecyclerView) binding.getClass().getDeclaredField("recycler").get(binding);
        } catch (NoSuchFieldException e) {
            try {
                RefreshRecyclerBinding includeRefreshRecycler = (RefreshRecyclerBinding) binding.getClass().getDeclaredField ("includeRefreshRecycler").get(binding);
                smartRefresh = includeRefreshRecycler.smartRefresh;
                recycler =  includeRefreshRecycler.recycler;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        smartRefresh.setRefreshHeader(new ClassicsHeader(getContext()));
        smartRefresh.setRefreshFooter(new ClassicsFooter(getContext()));
        smartRefresh.setOnRefreshLoadMoreListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        viewModel.onLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        viewModel.onRefresh();
    }

    private void finishFreshLoadmore(D result){
        if(result.code == 200){

            smartRefresh.setEnableRefresh(true);
            smartRefresh.finishRefresh();
            if(page == 1){
                adapter.getData().clear();
                adapter.setData(result.rows);
            }else{
                adapter.getData().addAll(result.rows);
                adapter.notifyDataSetChanged();
            }
            if(result.total <= adapter.getData().size()// 总数是否已经加载完
                    || result.rows.size() < SIZE // 最后一页数据的数量一般不满size
            ){//判断是否没有数据了
                smartRefresh.finishLoadMoreWithNoMoreData();
            }else {
                smartRefresh.finishLoadMore();
            }
        }else{
            smartRefresh.setEnableRefresh(true);
            smartRefresh.finishRefresh();
            smartRefresh.finishLoadMore();
        }
    }

    public A getAdapter() {
        return adapter;
    }
}
