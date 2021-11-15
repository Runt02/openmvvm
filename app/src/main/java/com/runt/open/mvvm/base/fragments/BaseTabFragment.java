package com.runt.open.mvvm.base.fragments;

import androidx.lifecycle.ViewModel;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.runt.open.mvvm.base.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 带有tablayout fragment封装
 * Created by Administrator on 2021/11/3 0003.
 */
public abstract class BaseTabFragment<B extends ViewBinding,VM extends ViewModel> extends BaseFragment<B,VM> {

    TabLayout tabLayout;
    FragmentAdapter fragmentAdapter;
    List<String> tabTitles = new ArrayList<>();
    ViewPager2 viewPager2;

    @Override
    public void initViews() {

        fragmentAdapter = new FragmentAdapter(activity);
        fragmentAdapter.setFragments(initFragments());
        setTabTitles(initTabTitles());
        //设置当前可见Item左右可见page数，次范围内不会被销毁
        //禁用预加载
        try {
            viewPager2 = (ViewPager2) binding.getClass().getDeclaredField("viewPager2").get(binding);
            tabLayout = (TabLayout) binding.getClass().getDeclaredField("tabLayout").get(binding);
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
