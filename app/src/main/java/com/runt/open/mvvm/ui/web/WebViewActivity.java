package com.runt.open.mvvm.ui.web;

import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.databinding.ActivityWebBinding;
import com.runt.open.mvvm.util.MyAnimations;
import com.runt.open.mvvm.util.MyLog;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-16.
 */
public class WebViewActivity extends BaseActivity<ActivityWebBinding, BaseViewModel> {

    private String url;
    private  int linProgressWidth;

    @Override
    public void initViews() {
        url = getIntent().getSerializableExtra(PARAMS_URL)+"";
        setTitle(getIntent().getSerializableExtra(PARAMS_TITLE)+"");
        initCompent();
    }

    @Override
    public void loadData() {

    }


    int count = 100;
    int index = 100;
    private void initCompent(){
        mBinding.browser.getSettings().setJavaScriptEnabled(true);
        mBinding.browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //跳转至拼接好的地址
        //mBaseHandler.sendMessage(msg);//http://192.168.5.156:8080/MyFinance/gd16/1.html
        mBinding.browser.loadUrl(url);
        mBinding.browser.setWebViewClient(new myWebViewClient());
        mBinding.browser.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view,final int newProgress) {
                MyLog.i("onProgressChanged","--newProgress:--"+newProgress);
                MyLog.i("onProgressChanged","--binding.viewProgressbar:--"+ mBinding.viewProgressbar.getWidth());
                final LayoutAnimationController.AnimationParameters animation= new LayoutAnimationController.AnimationParameters();   //得到一个LayoutAnimationController对象；
                animation.index =index++ ;
                animation.count = count++ ;
                if (newProgress == 100) {
                    MyAnimations.hideAnimaInSitu(mBinding.linProgressbar);
                    MyAnimations.makeViewMove(mBinding.viewProgressbar.getTranslationX(),0,0,0, mBinding.viewProgressbar);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.linProgressbar.setVisibility(View.GONE);
                        }
                    },MyAnimations.ANIMA_TIME);
                } else {

                    if (View.VISIBLE != mBinding.linProgressbar.getVisibility()) {
                        MyAnimations.showAnimaInSitu(mBinding.linProgressbar);
                        if(linProgressWidth==0){
                            final ViewTreeObserver vto = mBinding.linProgressbar.getViewTreeObserver();
                            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                public boolean onPreDraw() {
                                    linProgressWidth = mBinding.linProgressbar.getMeasuredWidth();
                                    mBinding.viewProgressbar.setTranslationX(0-linProgressWidth);
                                    mBinding.linProgressbar.getViewTreeObserver().removeOnPreDrawListener(this);
                                    return true;
                                }
                            });
                        }else{
                            mBinding.viewProgressbar.setTranslationX(0-linProgressWidth);
                        }

                    }
                    if(linProgressWidth!=0){
                        MyAnimations.makeViewMove(mBinding.viewProgressbar.getTranslationX(),0-linProgressWidth+linProgressWidth/100*newProgress,0,0, mBinding.viewProgressbar,MyAnimations.ANIMA_TIME*3);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MyAnimations.makeViewMove(mBinding.viewProgressbar.getTranslationX(), mBinding.viewProgressbar.getTranslationX()+300,0,0, mBinding.viewProgressbar,MyAnimations.ANIMA_TIME*10);
                            }
                        },MyAnimations.ANIMA_TIME*3);
                    }

                }
                super.onProgressChanged(view, newProgress);
            }

        });

        mBinding.browser.getSettings().setSavePassword(false);
        //Toast.makeText(mContext,"进入浏览器",Toast.LENGTH_SHORT).show();
        String Scale = String.valueOf(mBinding.browser.getScale());
        MyLog.i("Runt","--Scale:--"+Scale);
        int screenDensity=getResources().getDisplayMetrics().densityDpi;
        MyLog.i("Runt", "--screenDensity:--"+String.valueOf(screenDensity));  //60-160-240
    }




    private class myWebViewClient extends WebViewClient {

        /**
         * 每加载一张图片资源执行一次
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onLoadResource(view, url);
            //MyLog.i("WebView", "onLoadResource "+url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideProgressBar();
            //MyLog.i("WebView", "onPageFinished "+url);
        }

        /**
         * 获取页面跳转的链接
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            //MyLog.i("UrlLoading", "UrlLoading 正在跳转页面"+url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(mContext, "加载失败，请稍候再试", Toast.LENGTH_SHORT).show();
            hideProgressBar();
        }
    }

    private void hideProgressBar(){
        MyAnimations.hideAnimaInSitu(mBinding.linProgressbar);
        MyAnimations.makeViewMove(mBinding.viewProgressbar.getTranslationX(),0,0,0, mBinding.viewProgressbar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.linProgressbar.setVisibility(View.GONE);
            }
        },MyAnimations.ANIMA_TIME*2);
    }
}
