package com.runt.open.mvvm.data;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class Results {

    public static class StringApiResult extends HttpApiResult<String> { }

    //短信验证码
    public static class SmsResult { public String sms; }

    //资讯信息
    public class Message {
        public String id,title,content,cTime,cUName,cId;
    }

    //版本更新
    public class ApkVersion{
        public long id,code;
        public String version,detail,access;
    }

}
