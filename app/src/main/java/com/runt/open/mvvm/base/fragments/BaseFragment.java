package com.runt.open.mvvm.base.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.ViewModelFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * fragment 封装
 * Created by Administrator on 2021/10/28 0028.
 */
public abstract class BaseFragment<B extends ViewBinding,VM extends ViewModel> extends Fragment {

    protected BaseActivity activity;
    protected  B binding;
    protected  VM viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // get genericity "B"
        try {
            final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<B> entityClass = (Class<B>) type.getActualTypeArguments()[0];
            Method method = entityClass.getMethod("inflate", LayoutInflater.class,ViewGroup.class,boolean.class);//get method from name "inflate";
            binding = (B) method.invoke(entityClass,inflater,container,false);//execute method to create a objct of viewbind;
            Class<VM> vmClass = (Class<VM>) type.getActualTypeArguments()[1];
            viewModel = new ViewModelProvider(this,getViewModelFactory()).get(vmClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    public ViewModelProvider.Factory getViewModelFactory(){
        return ViewModelFactory.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity) getActivity();
        initViews();
    }

    public abstract void initViews();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
