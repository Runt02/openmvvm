package com.runt.open.mvvm.base.fragments;

import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.runt.open.mvvm.base.adapter.FragmentAdapter;
import com.runt.open.mvvm.base.model.BaseViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 带有tablayout fragment封装(带有viewpager的视图父类)
 * 继承此类，有效优化代码13行
 * 项目中有过多含有viewpager的fragment可调用，其他情况下不建议使用，优化代码量不佳
 * Created by Administrator on 2021/11/3 0003.
 */
@Deprecated
public abstract class BaseTabFragment<B extends ViewBinding,VM extends BaseViewModel> extends BaseFragment<B,VM> {

    TabLayout tabLayout;
    FragmentAdapter fragmentAdapter;
    List<String> tabTitles = new ArrayList<>();
    ViewPager2 viewPager2;

    @Override
    public void initViews() {
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
        viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        viewPager2.setCurrentItem(0);
        viewPager2.setUserInputEnabled(false); //true:滑动，false：禁止滑动
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(tabTitles.get(position))).attach();
    }

    @Override
    public void loadData() {
        fragmentAdapter = new FragmentAdapter(mActivity);
        fragmentAdapter.setFragments(initFragments());
        viewPager2.setAdapter(fragmentAdapter);
    }

    protected abstract List<String> initTabTitles();

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
