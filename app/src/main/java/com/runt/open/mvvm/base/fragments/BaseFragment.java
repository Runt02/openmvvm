package com.runt.open.mvvm.base.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.base.model.ViewModelFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * fragment 封装
 * Created by Administrator on 2021/10/28 0028.
 */
public abstract class BaseFragment<VB extends ViewBinding,VM extends BaseViewModel> extends Fragment {

    protected BaseActivity mActivity;
    protected  VB mBinding;
    protected  VM mViewModel;
    public static final int REQUEST_CODE_LOGOUT = 20009,/*退出*/
            REQUEST_CODE_ORDER = 10010,/*订单详情*/
            REQUEST_CODE_LOGIN = 20010,/*登录*/
            REQUEST_CODE_SCAN = 20011/*扫描请求*/,
            REQUEST_CODE_PIC = 20012,/*选择图片*/
            REQUEST_CODE_PERMISSION = 20013,/*请求权限*/
            REQUEST_CODE_SIGN = 20014,/*签到*/
            REQUEST_CODE_WITHDRAW = 22014,/*提现*/
            RESULT_CODE_FAILD = 4044/*失败*/,
            RESULT_CODE_SUCESS = 4046/*成功*/,
            RESULT_CODE_CANCEL = 4043/*取消*/;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // get genericity "B"
        try {
            //实例化viewbind,viewmodel
            final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<VB> entityClass = (Class<VB>) type.getActualTypeArguments()[0];
            Method method = entityClass.getMethod("inflate", LayoutInflater.class,ViewGroup.class,boolean.class);//get method from name "inflate";
            mBinding = (VB) method.invoke(entityClass,inflater,container,false);//execute method to create a objct of viewbind;
            Class<VM> vmClass = (Class<VM>) type.getActualTypeArguments()[1];
            mViewModel = new ViewModelProvider(this,getViewModelFactory()).get(vmClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //加载UI
        initViews();
        return mBinding.getRoot();
    }

    public void setOnClickListener(View.OnClickListener click,int... ids){
        for (int id: ids){
            getActivity().findViewById(id).setOnClickListener(click);
        }
    }
    public ViewModelProvider.Factory getViewModelFactory(){
        return ViewModelFactory.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mViewModel.onCreate(mActivity);
        loadData();
    }

    /**
     * 该方法内调用getActivity()为空，需要在loadData()中使用
     */
    public abstract void initViews();

    public abstract void loadData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

}
