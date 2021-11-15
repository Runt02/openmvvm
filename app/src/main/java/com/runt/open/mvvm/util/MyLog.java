package com.runt.open.mvvm.util;

import android.util.Log;

import com.runt.open.mvvm.BuildConfig;


/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-4-5.
 */
public class MyLog {


    public static void i(String tag,String msg){
        if(BuildConfig.DEBUG) {
            Log.i(tag, BuildConfig.BUILD_TYPE +" "+ msg);
        }
    }

    public static void e(String tag,String msg){
        if(BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }


    public static void d(String tag,String msg){
        if(BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }


    public static void v(String tag,String msg){
        if(BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

}
