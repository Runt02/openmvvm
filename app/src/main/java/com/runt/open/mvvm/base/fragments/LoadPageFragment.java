package com.runt.open.mvvm.base.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.adapter.BaseAdapter;
import com.runt.open.mvvm.base.model.LoadPageViewModel;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 含有上拉刷新的分页fragment
 * 继承此类，有效优化代码60行
 * 试用于 有下拉刷新，上拉加载等分页需求的界面
 * Created by Administrator on 2021/11/3 0003.
 */
public abstract class LoadPageFragment<VB extends ViewBinding,VM extends LoadPageViewModel,A extends BaseAdapter,RESULT> extends BaseFragment<VB,VM>  implements OnRefreshLoadMoreListener {

    protected int page;
    protected SmartRefreshLayout refresh;
    //适配器
    protected A adapter;

    @Override
    public void initViews() {
        try {
            Class<A> entityClass = (Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
            this.adapter = entityClass.newInstance();//实例化泛型
        } catch (Exception e) {
            e.printStackTrace();
        }
        RecyclerView recycler = mBinding.getRoot().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        refresh = mBinding.getRoot().findViewById(R.id.refresh);
        refresh.setRefreshHeader(new ClassicsHeader(getContext()));
        refresh.setRefreshFooter(new ClassicsFooter(getContext()));
        refresh.setOnRefreshLoadMoreListener(this);
        mViewModel.getLiveData().observe(this, (Observer<List<RESULT>>) list -> {
            adapter.showNull = true;
            if(page == 0){
                adapter.setData(list);
            }else{
                adapter.addData(list);
            }
            refresh.finishRefresh();
            //加载完毕
            if(list.size() < 10 || page > 0 && list.size() == 0){
                refresh.finishLoadMoreWithNoMoreData();
            }else{
                refresh.finishLoadMore();
            }
        });
        mViewModel.getLiveFailed().observe(this, o -> {
            refresh.finishRefresh();
            refresh.finishLoadMore();
            //校正page数值
            int size = adapter.getData().size();
            if(size/mViewModel.SIZE+1 < page){
                page--;
            }
        });
    }

    @Override
    public void loadData() {
        refresh.autoRefresh();
    }

    /**
     * 参数
     * @return
     */
    protected Map requestParams() {
        return new HashMap();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 0;
        mViewModel.requestData(page,requestParams());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        mViewModel.requestData(page,requestParams());
    }


    public A getAdapter() {
        return adapter;
    }
}
