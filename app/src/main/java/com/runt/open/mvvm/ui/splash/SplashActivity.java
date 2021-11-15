package com.runt.open.mvvm.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.runt.open.mvvm.MainActivity;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.databinding.ActivitySplashBinding;


/**
 * My father is Object, ites purpose of 启动页
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-4-16.
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding,SplashViewModel> {

    final String TAG = "WelcomeActivity";

    Handler handler = new Handler(){
        boolean started = false;
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(!started) {//确保该语句只执行一次
                started = true;
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    public void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        hideBottomUIMenu();
        viewModel.getSplashAd().observe(this, new Observer<TTSplashAd>() {
            @Override
            public void onChanged(TTSplashAd ttSplashAd) {
                binding.splashAdContainer.addView(ttSplashAd.getSplashView());
                //设置SplashView的交互监听器
                ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
                        handler.sendMessage(new Message());

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
                        handler.sendMessage(new Message());
                    }
                });
            }
        });
        viewModel.getTimeOut().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                handler.sendMessage(new Message());
            }
        });
        viewModel.applyTdAd(mContext);;//请求广告
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.splashAdContainer.removeAllViews();
    }
}
