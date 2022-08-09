package com.runt.open.mvvm.ui.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 延伸显示区域到刘海
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(attributes);
        }
        // 设置页面全屏显示 隐藏底部导航
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
