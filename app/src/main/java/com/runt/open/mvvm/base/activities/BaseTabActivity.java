package com.runt.open.mvvm.base.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.runt.open.mvvm.base.adapter.FragmentAdapter;
import com.runt.open.mvvm.base.fragments.BaseFragment;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 带有tablayout activity封装(带有viewpager的视图父类)
 * 继承此类，有效优化代码13行
 * 项目中有过多含有viewpager的activity可调用，其他情况下不建议使用，优化代码量不佳
 * Created by Administrator on 2021/11/4 0004.
 */
@Deprecated
public abstract class BaseTabActivity<B extends ViewBinding,VM extends BaseViewModel>  extends BaseActivity<B,VM> {

    TabLayout tabLayout;
    FragmentAdapter fragmentAdapter;
    List<String> tabTitles = new ArrayList<>();
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentAdapter = new FragmentAdapter(this);
        fragmentAdapter.setFragments(initFragments());
        setTabTitles(initTabTitles());
        //设置当前可见Item左右可见page数，次范围内不会被销毁
        //禁用预加载
        try {
            viewPager2 = (ViewPager2) mBinding.getClass().getDeclaredField("viewPager2").get(mBinding);
            tabLayout = (TabLayout) mBinding.getClass().getDeclaredField("tabLayout").get(mBinding);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        ;
        viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        viewPager2.setAdapter(fragmentAdapter);
        viewPager2.setCurrentItem(0);
        viewPager2.setUserInputEnabled(false); //true:滑动，false：禁止滑动
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(tabTitles.get(position))).attach();
    }

    /**
     * tablayout 标题列表
     * @return
     */
    protected abstract List<String> initTabTitles();

    /**
     * 初始化fragment列表
     * @return
     */
    protected abstract List<BaseFragment> initFragments();

    protected List<String> getTabTitles(){
        return tabTitles;
    }

    public FragmentAdapter getFragmentAdapter() {
        return fragmentAdapter;
    }

    public void setTabTitles(List<String> tabTitles) {
        if(tabTitles == null){
            this.tabTitles.clear();
        }
        this.tabTitles = tabTitles;
    }
    public void setTabTitles(String[] tabTitles) {
        if(tabTitles == null){
            this.tabTitles.clear();
        }
        setTabTitles(new ArrayList<>(Arrays.asList(tabTitles)));
    }
}
