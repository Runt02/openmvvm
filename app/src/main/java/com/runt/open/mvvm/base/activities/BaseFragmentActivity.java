package com.runt.open.mvvm.base.activities;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.base.fragments.BaseFragment;
import com.runt.open.mvvm.base.model.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有fragment切换的activity
 *  继承此类，有效优化代码22行
 * 试用于加载各种fragment需求的activity
 * Created by Runt (qingingrunt2010@qq.com) on 2022/8/13.
 */
public abstract class BaseFragmentActivity <VB extends ViewBinding,VM extends BaseViewModel>
        extends BaseActivity<VB,VM>{


    List<BaseFragment> fragments = new ArrayList<>();

    /**
     * 切换界面  要显示的fragnemt
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment){
        showFragment(fragments.indexOf(fragment));
    }

    /**
     * 切换界面  要显示的fragnemt
     * @param position
     */
    protected void showFragment(int position){
        if(position>fragments.size()-1|| position < 0){
            showToast("当前fragment不在队列中");
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for(int i = 0 ; i < fragments.size() ; i ++){
            if(i != position){
                transaction.hide(fragments.get(i));
            }
        }
        transaction.show(fragments.get(position));
        transaction.commit();
    }

    /**
     * 添加fragment
     * @param fragment
     */
    protected void addAndShowFragment(int viewId,BaseFragment fragment){
        if(fragments.contains(fragment)){
            showFragment(fragment);
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for(int i = 0 ; i < fragments.size() ; i ++){
            transaction.hide(fragments.get(i));
        }
        transaction.add(viewId,fragment).commit();
        fragments.add(fragment);
    }


}
