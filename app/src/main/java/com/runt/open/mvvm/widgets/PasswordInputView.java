package com.runt.open.mvvm.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.util.DimensionUtils;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2019-3-26.
 */
public class PasswordInputView  extends androidx.appcompat.widget.AppCompatEditText {
    private int textLength;

    private int borderColor;

    private float borderWidth;

    private float borderRadius;

    private int passwordLength;

    private int passwordColor;

    private float passwordWidth;

    private float passwordRadius;

    private Paint passwordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int defaultSplitLineWidth = 1;

    public PasswordInputView(Context context, AttributeSet attrs) {

        super(context, attrs);

        final Resources res = getResources();

        final int defaultBorderColor = res.getColor(R.color.cut_off_line);

        final float defaultBorderWidth = res.getDimension(R.dimen.dimen_1px);

        final float defaultBorderRadius = res.getDimension(R.dimen.radios);

        final int defaultPasswordLength = 6;

        final int defaultPasswordColor = res.getColor(R.color.txt_normal);

        final float defaultPasswordWidth = res.getDimension(R.dimen.dimen_6);

        final float defaultPasswordRadius = res.getDimension(R.dimen.dimen_6);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);

        try {

            borderColor = a.getColor(R.styleable.PasswordInputView_borderColor, defaultBorderColor);

            borderWidth = a.getDimension(R.styleable.PasswordInputView_borderWidth, defaultBorderWidth);

            borderRadius = a.getDimension(R.styleable.PasswordInputView_borderRadius, defaultBorderRadius);

            passwordLength = a.getInt(R.styleable.PasswordInputView_passwordLength, defaultPasswordLength);

            passwordColor = a.getColor(R.styleable.PasswordInputView_passwordColor, defaultPasswordColor);

            passwordWidth = a.getDimension(R.styleable.PasswordInputView_passwordWidth, defaultPasswordWidth);

            passwordRadius = a.getDimension(R.styleable.PasswordInputView_passwordRadius, defaultPasswordRadius);

        } finally {

            a.recycle();

        }

        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setStyle(Paint.Style.STROKE); //空心的
        borderPaint.setStrokeWidth(DimensionUtils.convertDpToPixel(1,getContext()));//线宽
        borderPaint.setColor(borderColor);

        passwordPaint.setStrokeWidth(passwordWidth);
        passwordPaint.setStyle(Paint.Style.FILL);

        passwordPaint.setColor(passwordColor);
        setBackground(null);
        setSingleLine(true);

    }

    @Override

    protected void onDraw(Canvas canvas) {

        int width = getWidth();

        int border = (int) DimensionUtils.convertDpToPixel(1,getContext());


        final float cha = (borderWidth - passwordWidth)/2;
        final float xCha = (width/passwordLength-borderWidth)/2;

        for (int i = 0; i < passwordLength; i++) {
            float x = width * i / passwordLength + xCha;
            canvas.drawRoundRect(new RectF(x, border, x+borderWidth, border+borderWidth), 10, 10, borderPaint);

        }

        // 密码

        float cx, cy = cha+passwordWidth/2 + border;

        for(int i = 0; i < textLength; i++) {

            cx = width * i / passwordLength + cha+passwordWidth/2 + xCha;

            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);

        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        this.textLength = text.toString().length();

        invalidate();

    }

    public int getBorderColor() {

        return borderColor;

    }

    public void setBorderColor(int borderColor) {

        this.borderColor = borderColor;

        borderPaint.setColor(borderColor);

        invalidate();

    }

    public float getBorderWidth() {

        return borderWidth;

    }

    public void setBorderWidth(float borderWidth) {

        this.borderWidth = borderWidth;

        borderPaint.setStrokeWidth(borderWidth);

        invalidate();

    }

    public float getBorderRadius() {

        return borderRadius;

    }

    public void setBorderRadius(float borderRadius) {

        this.borderRadius = borderRadius;

        invalidate();

    }

    public int getPasswordLength() {

        return passwordLength;

    }

    public void setPasswordLength(int passwordLength) {

        this.passwordLength = passwordLength;

        invalidate();

    }

    public int getPasswordColor() {

        return passwordColor;

    }

    public void setPasswordColor(int passwordColor) {

        this.passwordColor = passwordColor;

        passwordPaint.setColor(passwordColor);

        invalidate();

    }

    public float getPasswordWidth() {

        return passwordWidth;

    }

    public void setPasswordWidth(float passwordWidth) {

        this.passwordWidth = passwordWidth;

        passwordPaint.setStrokeWidth(passwordWidth);

        invalidate();

    }

    public float getPasswordRadius() {

        return passwordRadius;

    }

    public void setPasswordRadius(float passwordRadius) {

        this.passwordRadius = passwordRadius;

        invalidate();

    }
}
