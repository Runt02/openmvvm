package com.runt.open.mvvm.base.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.runt.open.mvvm.base.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2021/11/3 0003.
 */
public class FragmentAdapter extends FragmentStateAdapter {

    private List<BaseFragment> fragments = new ArrayList<>();

    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
    }

    public void removeFragment(BaseFragment fragment){
        this.fragments.remove(fragment);
    }

    public void removeFragment(int index){
        this.fragments.remove(index);
    }

    public void addFragment(BaseFragment fragment){
        fragments.add(fragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
