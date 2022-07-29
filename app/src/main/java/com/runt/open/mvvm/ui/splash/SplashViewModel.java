package com.runt.open.mvvm.ui.splash;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.runt.open.mvvm.base.model.BaseViewModel;

import java.util.Date;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public class SplashViewModel extends BaseViewModel {

    final String TAG = "SplashViewModel";
    long cTime = new Date().getTime(),limitTime = 2000;

    private MutableLiveData<Integer> timeOut = new MutableLiveData<>();


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


}
