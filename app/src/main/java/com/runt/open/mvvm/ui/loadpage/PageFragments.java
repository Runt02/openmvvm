package com.runt.open.mvvm.ui.loadpage;

import com.runt.open.mvvm.base.fragments.LoadPageFragment;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.databinding.RefreshRecyclerBinding;
import com.runt.open.mvvm.ui.adapter.MsgAdapter;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class PageFragments {

    public static class HomeFragment extends LoadPageFragment<RefreshRecyclerBinding, PageViewModels.HomeViewModel, MsgAdapter, Results.Message> {}
}
