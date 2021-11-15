package com.runt.open.mvvm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.runt.open.mvvm.util.MyLog;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.bugly.crashreport.CrashReport;

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
        CrashReport.initCrashReport(getApplicationContext(), "8d88679ae9", false);//注册bugly
        TTAdConfig.Builder builder = new TTAdConfig.Builder()
                .appId("5106813")
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(getString(R.string.app_name))
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                .supportMultiProcess(true) //是否支持多进程，true支持
                .asyncInit(true) ;//异步初始化sdk，开启可减少初始化耗时
        //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
        TTAdSdk.init(this, builder.build(), new TTAdSdk.InitCallback() {
            @Override
            public void success() {
                MyLog.i(TAG,"TTAdSdk success");
            }

            @Override
            public void fail(int i, String s) {
                MyLog.e(TAG,"TTAdSdk fail");
            }
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
