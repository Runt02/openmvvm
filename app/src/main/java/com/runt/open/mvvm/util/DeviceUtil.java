package com.runt.open.mvvm.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;


/**
 * Created by EDZ on 2018/1/30.
 */
public class DeviceUtil {
    public static final String TAG = "DeviceUtil";

    /**
     * 设备信息
     *
     * @param context
     */
    public static void getDisplay(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Version code is  \n");
        sb.append("设备的Android版本号:");//设备的Android版本号
        sb.append(getSDKInt() + " " + getSDKVersion() + "\t");//设备的Android版本号
        sb.append("设备型号:");//设备型号
        sb.append(getDeviceModel() + "\t");//设备型号
        sb.append("设备厂商:");//设备型号
        sb.append(getDeviceBrand() + "\t");//设备型号
        sb.append("程序版本号:" + getAppVersionCode(context) + " " + getAppVersionName(context) + "\t");//程序版本号
        sb.append("设备唯一标识符:" + getSerialNumber(context));
        sb.append("\n设备imei:" + getIMEI(context));
        String str = sb.toString() + " \n";
        str += getDisplayInfomation(context) + " \n";
        str += getDensity(context) + " \n";
        str += "屏幕大小：" + getScreenInch(context) + "英寸 \n";
        str += getAndroiodScreenProperty(context) + "\n";
        Log.i(TAG, str);
    }

    /**
     * Double类型保留指定位数的小数，返回double类型（四舍五入）
     * newScale 为指定的位数
     */
    private static double formatDouble(double d, int newScale) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 设备型号
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 设备厂商
     * @return
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 设备的Android版本号
     * @return
     */
    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 设备的Android版本号
     * @return
     */
    public static String getSDKVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 程序版本号
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 程序版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 屏幕像素px
     */
    @SuppressLint("NewApi")
    public static String getDisplayInfomation(Context context) {
        Point point = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
        Log.d(TAG, "the screen size is " + point.toString());
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(point);
        Log.d(TAG, "the screen real size is " + point.toString());
        return point.toString();
    }

    /**
     * 屏幕信息
     * @param context
     * @return
     */
    public static String getAndroiodScreenProperty(Context context) {
        int width = getScreenPixel(context).widthPixels;         // 屏幕宽度（像素）
        int height = getScreenPixel(context).heightPixels;       // 屏幕高度（像素）
        float density = getDensity(getScreenPixel(context));         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = getDpi(context);     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = getDp(context)[0];  // 屏幕宽度(dp)
        int screenHeight = getDp(context)[1];// 屏幕高度(dp)


        Log.d("h_bl", "屏幕宽度（像素）：" + width);
        Log.d("h_bl", "屏幕高度（像素）：" + height);
        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);  // 屏幕适配文件夹（例：layout-sw300dp），是以该属性为准则
        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);

        StringBuilder sb = new StringBuilder();
        sb.append("屏幕宽度（像素）：" + width + "\n");
        sb.append("屏幕高度（像素）：" + height + "\n");
        sb.append("屏幕密度（0.75 / 1.0 / 1.5）：" + density + "\n");
        sb.append("屏幕密度dpi（120 / 160 / 240）：" + densityDpi + "\n");
        sb.append("屏幕宽度（dp）：" + screenWidth + "\n");
        sb.append("屏幕高度（dp）：" + screenHeight + "\n");
        return sb.toString();

    }

    /**
     * 通知栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 屏幕密度dpi
     */
    public static int getDpi(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }


    /**
     * 屏幕像素px
     */
    @SuppressLint("NewApi")
    public static Point getDisplayPixel(Context context) {
        Point point = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
        Log.d(TAG, "the screen size is " + point.toString());
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(point);
        Log.d(TAG, "the screen real size is " + point.toString());
        return point;
    }

    /**
     * 屏幕像素px
     */
    public static DisplayMetrics getScreenPixel(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 屏幕dp
     */
    public static int[] getDp(Context context) {
        int width = getScreenPixel(context).widthPixels;         // 屏幕宽度（像素）
        int height = getScreenPixel(context).heightPixels;       // 屏幕高度（像素）
        float density = getDensity(getScreenPixel(context));         // 屏幕密度（0.75 / 1.0 / 1.5）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return new int[]{screenWidth, screenHeight};
    }

    /**
     * 屏幕密度
     */
    public static float getDensity(DisplayMetrics dm) {
        return dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）;
    }

    /**
     * 屏幕密度dpi
     */
    public static String getDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Log.d(TAG, "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
                " width: " + displayMetrics.widthPixels);
        return "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
                " width: " + displayMetrics.widthPixels;
    }


    /**
     * 屏幕尺寸inch
     */
    public static double getScreenInch(Context context) {

        try {
            int realWidth = 0, realHeight = 0;
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (Build.VERSION.SDK_INT < 17
                    && Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }

            return formatDouble(Math.sqrt((realWidth / metrics.xdpi) * (realWidth / metrics.xdpi) + (realHeight / metrics.ydpi) * (realHeight / metrics.ydpi)), 1);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }



    public static String getSerialNumber(Context context) {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= 28) {//9.0+
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "getMEID meid: READ_PHONE_STATE" );
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1567);
                } else {
                    serial = Build.getSerial();
                }
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            Log.e("e", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }

    public static  String getIMEI(Context context) {
        String deviceId = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "getMEID meid: READ_PHONE_STATE" );
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1567);
            } else {
                if (tm.getDeviceId() != null) {
                    deviceId = tm.getDeviceId();
                } else {
                    deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
            Log.d("deviceId--->", deviceId);
        }
        return "";
    }

    public static  String getMEID() {
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", String.class, String.class);

            String meid = (String) method.invoke(null, "ril.cdma.meid", "");
            if (!TextUtils.isEmpty(meid)) {
                Log.d(TAG, "getMEID meid: " + meid);
                return meid;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "getMEID error : " + e.getMessage());
        }
        return "";
    }

    /**
     * check the system is harmony os
     *
     * @return true if it is harmony os
     */
    public static boolean isHarmonyOS() {
        try {
            Class clz = Class.forName("com.huawei.system.BuildEx");
            Method method = clz.getMethod("getOsBrand");
            return "harmony".equals(method.invoke(clz));
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "occured ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "occured NoSuchMethodException");
        } catch (Exception e) {
            Log.e(TAG, "occur other problem");
        }
        return false;
    }

    /**
     * 获取鸿蒙系统版本号
     */
    public static String getHarmonyOsVersion() {
        if (isHarmonyOS()) {
            try {
                Class cls = Class.forName("android.os.SystemProperties");
                Method method = cls.getMethod("get", String.class);
                return method.invoke(cls, "ro.huawei.build.display.id").toString();
                //android.os.Build.DISPLAY
            } catch ( Exception e) {
            }
        }
        return "-1";
    }

    /**
     * 获取属性
     * @param property
     * @param defaultValue
     * @return
     */
    private static String getProp(String property, String defaultValue) {
        try {
            Class spClz = Class.forName("android.os.SystemProperties");
            Method method = spClz.getDeclaredMethod("get", String.class);
            String value = (String) method.invoke(spClz, property);
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            }
            return value;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
