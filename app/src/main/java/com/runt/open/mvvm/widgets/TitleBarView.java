package com.runt.open.mvvm.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.runt.open.mvvm.R;
import com.runt.open.mvvm.util.DimensionUtils;


/**
 * Created by Administrator on 2021/11/1 0001.
 */
public class TitleBarView extends View {
    Drawable leftDra,rightDra;
    private Rect mRect;       // 绘制区域
    private RectF leftClickRect,rightClickRect;//点击事件响应区域
    String titleText,rightText;
    @ColorInt int titleColor,rightTextColor;
    float titleSize,rightTextSize,rightPadding;
    Paint textPaint,rightTextPaint,drawPaint;
    int viewWidth,viewHeight;
    OnClickListener leftClick,rightClick;
    int touchStartX,touchStartY;


    public TitleBarView(Context context) {
        this(context,null);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidget(context.obtainStyledAttributes(attrs, R.styleable.TitleBarView));
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWidget(context.obtainStyledAttributes(attrs,R.styleable.TitleBarView));
    }

    public void initWidget(TypedArray array){
        leftDra = array.getDrawable(R.styleable.TitleBarView_leftDrawable);
        rightDra = array.getDrawable(R.styleable.TitleBarView_rightDrawable);
        titleText = array.getString(R.styleable.TitleBarView_titleText);
        titleColor = array.getColor(R.styleable.TitleBarView_titleTextColor, Color.BLACK);
        titleSize = array.getDimension(R.styleable.TitleBarView_titleTextSize, DimensionUtils.convertSpToPixel(getContext(),16));
        rightText = array.getString(R.styleable.TitleBarView_rightText);
        rightTextColor = array.getColor(R.styleable.TitleBarView_rightTextColor,Color.BLACK);
        rightTextSize = array.getDimension(R.styleable.TitleBarView_rightTextSize, DimensionUtils.convertSpToPixel(getContext(),14));
        rightPadding = array.getDimension(R.styleable.TitleBarView_rightDrawablePadding,10);

        int leftTint = array.getColor(R.styleable.TitleBarView_leftTint,-1);
        if(leftTint != -1) {
            setTint(leftDra,leftTint);
        }
        int rightTint = array.getColor(R.styleable.TitleBarView_rightTint,-1);
        if(rightTint != -1) {
            setTint(rightDra,rightTint);
        }
        textPaint = new Paint();
        textPaint.setAntiAlias(true); // 是否抗锯齿
        //mTextPaint.setAlpha(50); // 设置alpha不透明度，范围为0~255
        textPaint.setColor(titleColor);
        textPaint.setTextSize(titleSize);
        //        设置画笔属性
        textPaint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        //  paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        textPaint.setStrokeWidth(4);//设置画笔粗细


        rightTextPaint = new Paint();
        rightTextPaint.setAntiAlias(true); // 是否抗锯齿
        //mTextPaint.setAlpha(50); // 设置alpha不透明度，范围为0~255
        rightTextPaint.setColor(rightTextColor);
        rightTextPaint.setTextSize(rightTextSize);
        //        设置画笔属性
        rightTextPaint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        //  paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        rightTextPaint.setStrokeWidth(4);//设置画笔粗细

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mRect = new Rect(getPaddingLeft(),getPaddingTop(),getMeasuredWidth() - getPaddingRight(),getMeasuredHeight() - getPaddingBottom());
        Log.e("TitleBarView","onMeasure mRect:"+mRect);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("TitleBarView","onDraw mRect:"+mRect);
        if(leftDra != null){
            final Bitmap bitmap = ((BitmapDrawable) leftDra).getBitmap();
            float top = mRect.top+(viewHeight-bitmap.getHeight()*2f)/2;
            float left = mRect.left-bitmap.getWidth()*0.5f;
            leftClickRect = new RectF(left,top,left + (bitmap.getWidth()*2f),top + bitmap.getHeight()*2f);
            canvas.drawBitmap(bitmap,mRect.left,mRect.top+(viewHeight-bitmap.getHeight())/2,null);

        }
        if(rightDra != null){
            final Bitmap bitmap = ((BitmapDrawable) rightDra).getBitmap();
            float chaTop = (viewHeight-bitmap.getHeight())/2;
            float top = mRect.top+chaTop;
            float left = mRect.right-bitmap.getWidth();
            rightClickRect = new RectF(left-bitmap.getWidth()/2,top-bitmap.getHeight()/2,left+bitmap.getWidth()*2f,top+bitmap.getHeight()*1.5f);
            canvas.drawBitmap(bitmap,left,top,null);
        }
        if(titleText != null){
            final int textWidth = getTextWidth(textPaint, titleText);
            final int textHeight = getTextHeight(textPaint);
            final Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            canvas.drawText(titleText,mRect.left+((viewWidth-textWidth)/2),mRect.top+((viewHeight-textHeight)/2)+(0-fontMetrics.ascent),textPaint);
        }
        if(rightText != null){
            final int textWidth = getTextWidth(rightTextPaint, rightText);
            final int textHeight = getTextHeight(rightTextPaint);
            final Paint.FontMetrics fontMetrics = rightTextPaint.getFontMetrics();
            float left = mRect.right-textWidth;
            float top = mRect.top+((viewHeight-textHeight)/2)+(0-fontMetrics.ascent);
            rightClickRect = new RectF(left-textWidth/2,top-textHeight,left+textWidth*2f,top+textHeight/2);
            canvas.drawText(rightText,left,top,rightTextPaint);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = x;
                touchStartY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(isTouched(x,y,leftClickRect) && leftClick != null){
                    leftClick.onClick(this);
                }else if(isTouched(x,y,rightClickRect) && rightClick != null){
                    rightClick.onClick(this);
                }

                break;
        }
        return true;
    }

    /**
     * 是否为点击事件
     * @param x
     * @param y
     * @param rectF
     * @return
     */
    private boolean isTouched(int x, int y, RectF rectF){
        if(rectF == null){
            return false;
        }
        if(x < rectF.right && x > rectF.left &&
                touchStartX < rectF.right && touchStartX > rectF.left &&
                y < rectF.bottom && y > rectF.top &&
                touchStartY < rectF.bottom && touchStartY > rectF.top  ){
            return true;
        }
        return false;
    }
    public void setLeftDra(Drawable leftDra) {
        this.leftDra = leftDra;
        invalidate();
    }

    public void setRightDra(Drawable rightDra) {
        this.rightDra = rightDra;
        invalidate();
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        invalidate();
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        invalidate();
    }

    public void setTitleColor(@ColorInt int titleColor) {
        textPaint.setColor(titleColor);
        invalidate();
    }

    public void setRightTextColor(@ColorInt int rightTextColor) {
        rightTextPaint.setColor(rightTextColor);
        invalidate();
    }

    public void setTitleSize(int titleSize) {
        textPaint.setTextSize(DimensionUtils.convertSpToPixel(getContext(),titleSize));
        invalidate();
    }

    public void setRightTextSize(float rightTextSize) {
        rightTextPaint.setTextSize(DimensionUtils.convertSpToPixel(getContext(),rightTextSize));
        invalidate();
    }

    public void setRightPadding(float rightPadding) {
        this.rightPadding = rightPadding;
        invalidate();
    }

    public void setLeftClick(OnClickListener leftClick) {
        this.leftClick = leftClick;
    }

    public void setRightClick(OnClickListener rightClick) {
        this.rightClick = rightClick;
    }

    //第二个参数是一个数组.传进去个长度跟字符串长度相同的float数组,方法调用后,里边塞的是每个字符的长度.
    public  int  getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    //第二个参数是一个数组.传进去个长度跟字符串长度相同的float数组,方法调用后,里边塞的是每个字符的长度.
    public  int  getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height1 = fm.descent - fm.ascent;//文字的高度
        float height2 = fm.bottom - fm.top + fm.leading;//行高
        return (int) height2;
    }

    private void setTint(Drawable drawable, @ColorInt int color){
        if(drawable!= null){
            drawable.setTint(color);
        }
    }
}
