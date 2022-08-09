package com.runt.open.mvvm.ui.loadpage;

import com.runt.open.mvvm.base.model.LoadPageViewModel;
import com.runt.open.mvvm.ui.main.home.Message;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2022/8/9.
 */
public class PageViewModels {
    public class HomeViewModel extends LoadPageViewModel<Message> {
        @Override
        protected String requestUrl() {
            return "getMsgList";
        }
    }
}
