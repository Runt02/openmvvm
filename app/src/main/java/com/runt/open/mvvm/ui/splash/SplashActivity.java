package com.runt.open.mvvm.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.ImpViewModel;
import com.runt.open.mvvm.databinding.ActivitySplashBinding;
import com.runt.open.mvvm.ui.main.MainActivity;


/**
 * My father is Object, ites purpose of 启动页
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-4-16.
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding, ImpViewModel> {

    final String TAG = "WelcomeActivity";
    @Override
    public void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        hideBottomUIMenu();
    }

    @Override
    public void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

}
