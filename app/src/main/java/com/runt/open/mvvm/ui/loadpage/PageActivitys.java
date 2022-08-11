package com.runt.open.mvvm.ui.loadpage;

import com.runt.open.mvvm.base.activities.LoadPageActivity;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.databinding.RefreshRecyclerBinding;
import com.runt.open.mvvm.ui.adapter.CoinTransAdapter;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class PageActivitys {

    //金币记录
    public class CoinRecordActivity extends LoadPageActivity<RefreshRecyclerBinding, PageViewModels.CoinRecordViewModel, CoinTransAdapter, Results.CustomCoin>{
        @Override
        protected String initTitle() {
            return "金币记录";
        }
    }

}
