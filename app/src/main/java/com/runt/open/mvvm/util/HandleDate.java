package com.runt.open.mvvm.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class HandleDate {

    private static SimpleDateFormat secondsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat timesdf = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat hoursdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private static SimpleDateFormat minutesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 获取当前时间的String类型
     */
    public static long getDateToLong() {
        return new Date().getTime();
    }

    /**
     * 获取指定时间的long类型
     */
    public static long getDateToLong(Date date) {

        return date.getTime();
    }

    /**
     * 获取指定时间的long类型
     *
     * @throws ParseException
     */
    public static long getDateToLong(String datestr) throws ParseException {

        return datesdf.parse(datestr).getTime();
    }

    /**
     * 将long类型的时间转换成只有日期的int类型
     */
    public static int getDateToInt(long datetime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = getLongToDate(datetime);
        String format = sdf.format(date);
        return Integer.parseInt(format);
    }

    /**
     * 将指定long类型的日期转换为date
     */
    public static Date getLongToDate(Long datetime) {
        return new Date(datetime);
    }

    /**
     * 将指定long类型的日期转换为string时间精确到秒
     */
    public static String getLongToSecond(Long datetime) {
        Date date = new Date(datetime);
        return secondsdf.format(date);
    }

    public static long getDateTimeToLong(String datetime) {
        try {
            return secondsdf.parse(datetime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将指定long类型的日期转换为string时间 只显示 时分秒
     */
    public static String getLongToTime(Long datetime) {
        String datestr = "";
        Date date = null;
        try {
            date = new Date(datetime);
            datestr = timesdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * 将指定long类型的日期转换为string时间只显示日期
     */
    public static String getLongToDatestr(Long datetime) {
        String datestr = "";
        Date date = null;
        try {
            date = new Date(datetime);
            datestr = datesdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * 将指定long类型的日期转换为string时间精确到小时
     */
    public static String getLongToHour(Long datetime) {
        String datestr = "";
        Date date = null;
        try {
            date = new Date(datetime);
            datestr = hoursdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * 将指定long类型的日期转换为string日期
     */
    public static String getLongToSimpleDate(Long datetime) {
        String datestr = "";
        Date date = null;
        try {
            date = new Date(datetime);
            datestr = datesdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * 根据毫秒时间戳来格式化字符串 今天显示今天、昨天显示昨天、前天显示前天. 早于前天的显示具体年-月-日，如2017-06-12；
     *
     * @param timeStamp 毫秒值
     * @return 今天 昨天 前天 或者 yyyy-MM-dd HH:mm:ss类型字符串
     */
    public static String format(long timeStamp) {
        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
        if (timeStamp >= todayStartMillis) {
            return "今天";
        }
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if (timeStamp >= yesterdayStartMilis) {
            return "昨天";
        }
        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;
        if (timeStamp >= yesterdayBeforeStartMilis) {
            return "前天";
        }
        //	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(timeStamp));
    }

    /**
     * 根据时间戳来判断当前的时间是几天前,几分钟,刚刚
     *
     * @param long_time
     * @return
     */

    public static String getTimeStateNew(String long_time) {
        String long_by_13 = "1000000000000";
        String long_by_10 = "1000000000";
        if (Long.valueOf(long_time) / Long.valueOf(long_by_13) < 1) {
            if (Long.valueOf(long_time) / Long.valueOf(long_by_10) >= 1) {
                long_time = long_time + "000";
            }
        }
        return getTimeStateNew(long_time);
    }
    public static String getTimeStateNew(Date long_time) {
        return getTimeStateNew(long_time.getTime() );
    }
    public static String getTimeStateNew(Long long_time) {
        Timestamp time = new Timestamp(Long.valueOf(long_time));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //	     System.out.println("传递过来的时间:"+format.format(time));
        //	     System.out.println("现在的时间:"+format.format(now));
        long day_conver = 1000 * 60 * 60 * 24;
        long hour_conver = 1000 * 60 * 60;
        long min_conver = 1000 * 60;
        long time_conver = now.getTime() - time.getTime();
        long temp_conver;
        //	     System.out.println("天数:"+time_conver/day_conver);
        if ((time_conver / day_conver) < 3) {
            temp_conver = time_conver / day_conver;
            if (temp_conver <= 2 && temp_conver >= 1) {
                return temp_conver + "天前";
            } else {
                temp_conver = (time_conver / hour_conver);
                if (temp_conver >= 1) {
                    return temp_conver + "小时前";
                } else {
                    temp_conver = (time_conver / min_conver);
                    if (temp_conver >= 1) {
                        return temp_conver + "分钟前";
                    } else {
                        return "刚刚";
                    }
                }
            }
        } else {
            return format.format(time);
        }
    }


}
