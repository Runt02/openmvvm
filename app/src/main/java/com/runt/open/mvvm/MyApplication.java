package com.runt.open.mvvm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.runt.open.mvvm.listener.CrashHandler;
import com.runt.open.mvvm.util.MyLog;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2021/10/28 0028.
 */
public class MyApplication extends Application {

    final  String TAG = "MyApplication";
    List<Activity> activities = new ArrayList<>();
    private Activity currentActivity;//当前activity
    private boolean isInfront; //是否前台运行
    private static MyApplication application;
    public static MyApplication getApplication() {
        return application;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.i(TAG,"onCreate");
        application = this;
        //CrashReport.initCrashReport(getApplicationContext(), "8d88679ae9", false);//注册bugly
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                MyLog.d(TAG,"onActivityCreated "+activity.getClass().getSimpleName());
                if(!activities.contains(activity)){
                    activities.add(activity);
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                MyLog.d(TAG,"onActivityStarted "+activity.getClass().getSimpleName());
                currentActivity = activity;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                MyLog.d(TAG,"onActivityResumed "+activity.getClass().getSimpleName());
                currentActivity = activity;
                isInfront = true;
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                MyLog.d(TAG,"onActivityPaused "+activity.getClass().getSimpleName());

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                MyLog.d(TAG,"onActivityStopped "+activity.getClass().getSimpleName());

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
                MyLog.d(TAG,"onActivitySaveInstanceState "+activity.getClass().getSimpleName());
                isInfront = false;

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                MyLog.d(TAG,"onActivityDestroyed "+activity.getClass().getSimpleName());
                if(activities.contains(activity)){
                    activities.remove(activity);
                }
            }
        });

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext(), () -> {
            for(Activity activity : activities){
                activity.finish();
            }
            Process.killProcess(Process.myPid());
            System.exit(0);
        });
    }

    /**
     * 退出程序
     */
    public void quitApp(){
        for(Activity activity:activities){
            activity.finish();
        }
        System.exit(0);
    }


    public boolean isInfront(){
        return isInfront;
    }
}
