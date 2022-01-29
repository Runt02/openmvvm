package com.runt.open.mvvm.util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.runt.open.mvvm.R;

/**
 * Created by Administrator on 2017/11/30.
 */
public class MyAnimations {

    public static final int ANIMA_TIME = 300;
    public static final float MOVE_SPACE = 1;
    public static final float SITU = 0;



    /**
     *  移动控件
     * @param x                x初始位置
     * @param distanceX     x移动的距离
     * @param y             y初始位置
     * @param distanceY     y移动的距离
     * @param view
     */
    public static void makeViewMove(float x ,float  distanceX , float y ,float  distanceY, View view){
        setAnimator("translationY", y, distanceY,view,ANIMA_TIME);
        setAnimator("translationX", x, distanceX,view,ANIMA_TIME);
    }
    /**
     *  移动控件
     * @param x                x初始位置
     * @param distanceX     x移动的距离
     * @param y             y初始位置
     * @param distanceY     y移动的距离
     * @param view
     */
    public static void makeViewMove(float x , float  distanceX , float y , float  distanceY, View view, int animTime){
        setAnimator("translationY", y, distanceY,view,animTime);
        setAnimator("translationX", x, distanceX,view,animTime);
    }

    private static  void setAnimator(String attribute, float from, float to, View view, int animTime){
        ObjectAnimator.ofFloat(view, attribute, from, to).setDuration(animTime).start();
    }
    public static void setLayoutMargin(View view, int left, int top, int right, int bottom)
    {
        //克隆view的width、height、margin的值生成margin对象
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        //设置新的边距
        margin.setMargins(left, top, right, bottom);
        //把新的边距生成layoutParams对象
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        //设制view的新的位置
        view.setLayoutParams(layoutParams);
    }
    /**
     * 原地不动
     * @return
     */
    private static TranslateAnimation makeInSitu(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,SITU,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,SITU);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }


    /**
     * 从顶部显示
     * @return
     */
    private static TranslateAnimation makeInFromTop(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,SITU,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,0-MOVE_SPACE,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,SITU);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }

    /**
     * 从底部显示
     * @return
     */
    private static TranslateAnimation makeInFromBottom(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,SITU,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,MOVE_SPACE,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,SITU);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }

    /**
     * 从左侧显示
     * @return
     */
    private static TranslateAnimation makeInFromLeft(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0-MOVE_SPACE,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,SITU);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }

    /**
     * 从右侧显示
     * @return
     */
    private static TranslateAnimation makeInFromRight(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,MOVE_SPACE,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,SITU);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }


    /**
     * 向上隐藏
     * @return
     */
    private static TranslateAnimation makeOutToTop(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,SITU,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,0-MOVE_SPACE);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }

    /**
     * 向左隐藏
     * @return
     */
    private static TranslateAnimation makeOutToLeft(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,SITU,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,0-MOVE_SPACE,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,SITU);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }

    /**
     * 向右隐藏
     * @return
     */
    private static TranslateAnimation makeOutToRight(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,SITU,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,MOVE_SPACE,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,SITU);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }

    /**
     * 向下隐藏
     * @return
     */
    private static TranslateAnimation makeOutToButtom(){
        TranslateAnimation mAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,SITU,//大于0 则从右向当前位置移动，反之则从左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从当前位置向右移动，反之则向左
                Animation.RELATIVE_TO_SELF,SITU,//大于0 则从下方向当前位置，反之则从上方
                Animation.RELATIVE_TO_SELF,MOVE_SPACE);//大于0从当前位置向下移动，反之则向上方
        mAction.setDuration(ANIMA_TIME);
        return mAction;
    }



    /***
     * 动画显示 从右向左左显示
     *
     * @param view
     */
    public static void showAnimaRightToLeft(View view) {
        view.setVisibility(View.VISIBLE);
        Animation mAni;
        mAni =  makeInFromRight();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }


    /***
     * 动画显示 从右向左左显示
     *
     * @param view
     */
    public static void showAnimaLeftToRight(View view) {
        view.setVisibility(View.VISIBLE);
        Animation mAni;
        mAni =  makeInFromLeft();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }

    /**
     * 动画隐藏 从下往上
     *
     * @param view
     */
    public static void hideAnimaBottomToTop(View view) {
        view.setVisibility(View.INVISIBLE);
        Animation mAni;
        mAni = makeOutToTop();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }

    /**
     * 动画隐藏 从左往右
     *
     * @param view
     */
    public static void hideAnimaLeftToRight(View view) {
        view.setVisibility(View.INVISIBLE);
        Animation mAni;
        mAni = makeOutToRight();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }

    /**
     * 动画隐藏 从右往左
     *
     * @param view
     */
    public static void hideAnimaRightToLeft(View view) {
        view.setVisibility(View.INVISIBLE);
        Animation mAni;
        mAni = makeOutToLeft();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }


    /**
     * 动画隐藏 原地
     *
     * @param view
     */
    public static void hideAnimaInSitu(View view) {
        view.setVisibility(View.INVISIBLE);
        Animation mAni;
        mAni = makeInSitu();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }


    /**
     * 动画显示 原地
     *
     * @param view
     */
    public static void showAnimaInSitu(View view) {
        view.setVisibility(View.VISIBLE);
        Animation mAni;
        mAni = makeInSitu();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }



    /**
     * 动画显示 从上往下走
     *
     * @param view
     */
    public static void showAnimaTopToBottom(View view) {
        view.setVisibility(View.VISIBLE);
        Animation mAni;
        mAni = makeInFromTop();
        mAni.setDuration(ANIMA_TIME);
        view.setAnimation(mAni);
    }

    /**
     *  冒泡式显示控件
     * @param view
     */
    public static void showReBound(final View view){
        Log.i("","showReBound view:"+view);
        view.setVisibility(View.GONE);
        showAnimaInSitu(view);
        SpringSystem springSystem = SpringSystem.create();
        final Spring spring = springSystem.createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100,7));//qcTension拉力和qcFriction摩擦力参数
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = value;
                view.setScaleX(scale);
                view.setScaleY(scale);
            }
        });
        spring.setEndValue(1);//控件拉伸收缩的倍率
    }

    /**
     * 冒泡式放大控件
     * @param view
     */
    public static void showReBoundBig(final View view){
        Log.i("","showReBound view:"+view);
        view.setVisibility(View.GONE);
        showAnimaInSitu(view);
        SpringSystem springSystem = SpringSystem.create();
        final Spring spring = springSystem.createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100,7));//qcTension拉力和qcFriction摩擦力参数
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = value;
                view.setScaleX(scale);
                view.setScaleY(scale);
            }
        });
        spring.setEndValue(3);//控件拉伸收缩的倍率
    }

    /**
     * 收缩式 隐藏
     * @param view
     * @param context
     */
    public static void hideReBound(final View view, final Context context){
        /*showReBound(view);
        @SuppressLint("ResourceType") Animator animator = AnimatorInflater.loadAnimator(context, R.anim.anima_make_none);
        animator.setTarget(view);
        animator.start();*/

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                float value = (float) msg.obj;
                if(value>2){
                    @SuppressLint("ResourceType") Animator animator = AnimatorInflater.loadAnimator(context, R.anim.anima_make_none);
                    animator.setTarget(view);
                    animator.start();
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation, boolean isReverse) {
                            MyLog.i("hideReBound","onAnimationEnd "+animation+" "+isReverse);
                            view.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationStart(Animator animation, boolean isReverse) {}
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            MyLog.i("hideReBound","onAnimationEnd "+animator);
                            view.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationCancel(Animator animator) {}
                        @Override
                        public void onAnimationRepeat(Animator animator) { }
                    });
                    hideAnimaInSitu(view);
                }else {
                    value = 1f + (value);
                    view.setScaleX(value);
                    view.setScaleY(value);
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                try {
                    int sleep = 10;
                    for(int i =0 ; i < 100 ; i+=sleep){
                        sleep(sleep);
                        Message msg = new Message();
                        msg.obj = (float)i/100/3;
                        handler.sendMessage(msg);
                    }

                    Message msg = new Message();
                    msg.obj = 3f;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    static int time = 0;
    public static void animaScale(final Context context, final View view, final float x, final float y){

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                float value = (float) msg.obj;
                value = 1f + (value);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        };
        new Thread(){
            @Override
            public void run() {
                try {
                    int sleep = 10;
                    for(int i =0 ; i < 100 ; i+=sleep){
                        sleep(sleep);
                        Message msg = new Message();
                        msg.obj = (float)i/100/3;
                        handler.sendMessage(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     *  冒泡式拉伸控件
     * @param view
     */
    public static void scaleReBoundX(final View view, final int size){
        Log.i("","scaleReBound view:"+view);
        SpringSystem springSystem = SpringSystem.create();
        final Spring spring = springSystem.createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100,7));//qcTension拉力和qcFriction摩擦力参数
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = value;
                view.setScaleX(scale);
                view.setScaleY(scale);
            }
        });
        spring.setEndValue(size);//控件拉伸收缩的倍率
    }

    public static void scalXAnima(final View view, float from, float to){
        setAnimator("scaleX",from,to,view,ANIMA_TIME);
    }
}
