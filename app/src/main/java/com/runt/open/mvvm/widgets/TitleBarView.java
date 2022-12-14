package com.runt.open.mvvm.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
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
    Paint textPaint,rightTextPaint;
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

        textPaint = initPaint(titleColor,titleSize,4);

        rightTextPaint = initPaint(rightTextColor,rightTextSize,4);

    }

    private Paint initPaint(int color,float size,int stroke){
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 是否抗锯齿
        //mTextPaint.setAlpha(50); // 设置alpha不透明度，范围为0~255
        paint.setColor(color);
        paint.setTextSize(size);
        //        设置画笔属性
        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        //  paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        paint.setStrokeWidth(stroke);//设置画笔粗细
        return paint;
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
            //点击的区域
            leftClickRect = drawBitmap(leftDra,canvas,getPaddingLeft());

        }
        if(rightDra != null){
            //点击的区域
            rightClickRect = drawBitmap(rightDra,canvas,getPaddingRight()+viewWidth);
        }
        if(titleText != null){
            final int textWidth = getTextWidth(textPaint, titleText);
            final float[] textHeight = getTextHeight(textPaint);
            float top = (getHeight()-textHeight[0])/2-textPaint.getFontMetrics().ascent;
            canvas.drawText(titleText,mRect.left+((viewWidth-textWidth)/2),top,textPaint);
        }
        if(rightText != null){
            final int textWidth = getTextWidth(rightTextPaint, rightText);
            final float[] textHeight = getTextHeight(rightTextPaint);
            float chaleft = textWidth*0.5f;
            float chaTop = textHeight[0]*0.5f;
            float left = mRect.right-textWidth;
            float top = (getHeight()-textHeight[0])/2-textPaint.getFontMetrics().ascent;
            rightClickRect = new RectF(left-chaleft,top-chaTop,left +chaleft*3,top + chaTop*3);
            canvas.drawText(rightText,left,top,rightTextPaint);
        }
    }

    /**
     * 绘制图片
     * @param drawable
     * @param canvas
     * @return  点击的区域
     */
    private RectF drawBitmap(Drawable drawable,Canvas canvas,float left){
        final Bitmap bitmap = drawableToBitmap(drawable);
        if(left>viewWidth){//右侧
            left -= bitmap.getWidth();
        }
        float chaTop = bitmap.getHeight()*0.5f;
        float chaleft = bitmap.getWidth()*0.5f;
        //绘制的位置
        float top = (getHeight()-bitmap.getHeight())/2;
        canvas.drawBitmap(bitmap,left,top,null);
        return new RectF(left-chaleft,top-chaTop,left +chaleft*3,top + chaTop*3);
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
    public float[] getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height1 = fm.descent - fm.ascent;//文字的高度
        float height2 = fm.bottom - fm.top + fm.leading;//行高
        return new float[]{height1,height2};
    }

    private void setTint(Drawable drawable, @ColorInt int color){
        if(drawable!= null){
            drawable.setTint(color);
        }
    }


    private Bitmap drawableToBitmap(Drawable drawable){
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
