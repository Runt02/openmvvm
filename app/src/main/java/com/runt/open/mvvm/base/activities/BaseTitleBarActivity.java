package com.runt.open.mvvm.base.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.widgets.TitleBarView;

/**
 * 带有标题栏的activity封装
 * Created by Administrator on 2021/11/2 0002.
 */
public abstract class BaseTitleBarActivity<B extends ViewBinding,VM extends BaseViewModel> extends BaseActivity<B,VM> {
    TitleBarView titleBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            titleBarView = (TitleBarView) binding.getClass().getDeclaredField("titleBar").get(binding);
            titleBarView.setLeftClick(v -> onTitleLeftClick());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    protected void setTitle(String text){
        titleBarView.setTitleText(text);
    }
    
    protected void onTitleLeftClick(){
        onBackKeyDown();
    }

    protected void setTitleRight(String text){
        titleBarView.setRightText(text);
        titleBarView.setRightDra(null);
    }

    protected void setTitleRight(Drawable drawable){
        titleBarView.setRightText(null);
        titleBarView.setRightDra(drawable);
    }

    @Override
    public void setStatusBarTransparent(boolean isBlack) {
        super.setStatusBarTransparent(isBlack);
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) titleBarView.getLayoutParams();
        layoutParams.topMargin = layoutParams.topMargin+getStatusBarHeight();
        titleBarView.setLayoutParams(layoutParams);

    }
}
