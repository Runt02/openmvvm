package com.runt.open.mvvm.ui.splash;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.runt.open.mvvm.base.model.BaseViewModel;

import java.util.Date;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public class SplashViewModel extends BaseViewModel {

    final String TAG = "SplashViewModel";
    long cTime = new Date().getTime(),limitTime = 2000;

    private MutableLiveData<TTSplashAd> splashAd = new MutableLiveData<>();
    private MutableLiveData<Integer> timeOut = new MutableLiveData<>();

    public MutableLiveData<TTSplashAd> getSplashAd() {
        return splashAd;
    }

    public MutableLiveData<Integer> getTimeOut() {
        return timeOut;
    }

    public void countdown(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timeOut.setValue(0);
            }
        },limitTime);
    }

    /**
     * 请求广告
     * @param context
     */
    public void applyTdAd(Context context){
        countdown();
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("887382769")//广告id
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080,1920)
                //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
                //.setExpressViewAcceptedSize(expressViewWidth, expressViewHeight)
                .build();
        mTTAdNative.loadSplashAd(adSlot,new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.i(TAG,"code:"+i+" message:"+s);
            }

            @Override
            public void onTimeout() {
                Log.i(TAG,"超时");
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ttSplashAd) {
                Log.d(TAG, "开屏广告请求成功");

                long waitTime = limitTime - (new Date().getTime() - cTime);
                if(waitTime > 0){//是否超过限定时间  没有超时则继续
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashAd.setValue(ttSplashAd);
                        }
                    }, waitTime > 0 ? waitTime : 0);
                }
            }
        });
    }

}
