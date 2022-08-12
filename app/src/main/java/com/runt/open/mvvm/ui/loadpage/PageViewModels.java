package com.runt.open.mvvm.ui.loadpage;

import com.runt.open.mvvm.base.model.LoadPageViewModel;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.PageResult;
import com.runt.open.mvvm.data.Results;
import io.reactivex.Observable;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class PageViewModels {
    public static class HomeViewModel extends LoadPageViewModel<Results.Message> {
        @Override
        public Observable<HttpApiResult<PageResult<Results.Message>>> request(int page, Object... objects) {
            return commonApi.getMsgList(page,SIZE);
        }
    }

    public static class CoinRecordViewModel extends LoadPageViewModel<Results.CustomCoin>{
        @Override
        public Observable<HttpApiResult<PageResult<Results.CustomCoin>>> request(int page, Object... objects) {
            return commonApi.getCoinRecord(page,SIZE,0);
        }
    }
}
