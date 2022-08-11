package com.runt.open.mvvm.ui.loadpage;

import com.runt.open.mvvm.base.model.LoadPageViewModel;
import com.runt.open.mvvm.data.Results;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class PageViewModels {
    public static class HomeViewModel extends LoadPageViewModel<Results.Message> {
        @Override
        protected String requestUrl() {
            return "getMsgList";
        }
    }

    public static class CoinRecordViewModel extends LoadPageViewModel<Results.CustomCoin>{
        @Override
        protected String requestUrl() {
            return "coinRecord";
        }
    }
}
