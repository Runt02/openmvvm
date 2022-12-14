package com.runt.open.mvvm.base.activities;

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
 * 含有上拉刷新的分页Activity
 * 继承此类，有效优化代码60行
 * 试用于 有下拉刷新，上拉加载等分页需求的界面
 * Created by Administrator on 2021/11/4 0004.
 */
public abstract class LoadPageActivity<VB extends ViewBinding,VM extends LoadPageViewModel,A extends BaseAdapter,RESULT>
        extends BaseActivity<VB,VM>  implements OnRefreshLoadMoreListener {
    protected int page;
    protected SmartRefreshLayout refresh;
    //适配器
    protected A adapter;

    protected String initTitle(){return null;}

    @Override
    public void initViews() {
        if(initTitle() != null) {
            setTitle(initTitle());
        }
        try {
            Class<A> entityClass = (Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
            this.adapter = entityClass.newInstance();//实例化泛型
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh = mBinding.getRoot().findViewById(R.id.refresh);
        refresh.setRefreshHeader(new ClassicsHeader(mContext));
        refresh.setRefreshFooter(new ClassicsFooter(mContext));
        refresh.setOnRefreshLoadMoreListener(this);
        RecyclerView recycler = mBinding.getRoot().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);
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
