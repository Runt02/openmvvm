package com.runt.open.mvvm.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * My father is Object, ites purpose of   正方形控件
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2019-3-24.
 */

public class QuadrateLinearLayout extends LinearLayout {

    public QuadrateLinearLayout(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public QuadrateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuadrateLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();
        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
