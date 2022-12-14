package com.runt.open.mvvm.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2021/11/2 0002.
 */
public class DimensionUtils {
    /**
     * dp获取dip
     * @param dp
     * @return
     */
    public static float convertDpToPixel(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (dp * displayMetrics.density);
    }

    /***
     * px获取dip
     * @param pixel
     * @return
     */
    public static float convertPixelToDp(int pixel,Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (pixel / displayMetrics.density);
    }
    /**
     * 把pix值转换为sp
     *
     * @return
     */
    public static float convertPixelToSp( float pixValue,Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return  pixValue / fontScale + 0.5f;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param context
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static float convertSpToPixel(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }
}
