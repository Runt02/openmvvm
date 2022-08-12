package com.runt.open.mvvm.data;

import com.runt.open.mvvm.ui.login.UserBean;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    //金币记录
    public class CustomCoin{
        public String id;
        public UserBean toUser,fromUser;
        public int count,after,before,type;
        public String cTime;
        /**
         * 备注
         **/
        public Object remark;
    }


    public static class Mycalendar {

        String day;
        int style;
        long dateTime;
        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public long getDateTime() {
            return dateTime;
        }

        public void setDateTime(long dateTime) {
            this.dateTime = dateTime;
        }

        SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
        @Override
        public String toString() {
            return "Mycalendar{" +
                    "day='" + day + '\'' +
                    ", style=" + style +
                    ", dateTime=" + datesdf.format(new Date(dateTime)) +
                    '}';
        }
    }
}
