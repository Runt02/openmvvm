package com.runt.open.mvvm.listener;

import android.view.View;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-4-14.
 */
public abstract class CustomClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 500L;

    public CustomClickListener() {

    }

    public CustomClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick(v);
            mLastClickTime = nowTime;
        } else {
            // 快速点击事件
            onFastClick(v);
        }
    }

    protected abstract void onSingleClick(View view);
    protected void onFastClick(View v){};
}