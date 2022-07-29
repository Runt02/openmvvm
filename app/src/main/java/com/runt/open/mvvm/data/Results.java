package com.runt.open.mvvm.data;

import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.ui.main.home.Message;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2022/1/29.
 */
public class Results {

    public static class LoggedInUser extends HttpApiResult<UserBean> { }

    public static class StringApiResult extends HttpApiResult<String> { }

    public class MessageResult extends PageResult<Message>{}
}
