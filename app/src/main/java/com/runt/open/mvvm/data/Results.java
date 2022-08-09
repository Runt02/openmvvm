package com.runt.open.mvvm.data;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2022/1/29.
 */
public class Results {

    public static class StringApiResult extends HttpApiResult<String> { }

    //短信验证码
    public static class SmsResult { public String sms; }

    //资讯信息
    public class Message {
        public String id,title,content,cTime;
    }

}
