package com.runt.open.mvvm.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.collection.ArraySet;

import java.util.Set;

/**
 * PreferencesUtils, easy to get or put data
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-3-6
 */
public class PreferencesUtils {

    public static final String PREFERENCE_NAME="zipper";
    public static final String PROJECT = "project";
    public static final String USER = "user";
    public static final String VISITOR = "visitor";

    private PreferencesUtils() {
        throw new AssertionError();
    }

    public static boolean clearData(Context context, String keyShared){
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        return settings.edit().clear().commit();
    }


    public static boolean clearData(Context context, String key, String keyShared){
        putString(context,key,null,keyShared);
        return true;
    }



    /**
     * remove key preferences
     *
     * @param context
     * @param key The name of the preference to modify
     * @return True if the new values were successfully written to persistent storage.
     */
    public static void  removeKey(Context context, String key, String keyShared){

        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
    }

    /**
     * remove value preferences
     *
     * @param context
     * @param value The name of the preference to modify
     * @return True if the new values were successfully written to persistent storage.
     */
    public static void  removeValue(Context context, String value, String keyShared){
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(value);
    }


    /**
     * put string preferences
     *
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putString(Context context, String key, String value, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get string preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     *         name that is not a string
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key, String keyShared) {
        return getString(context, key, null,keyShared);
    }

    /**
     * get string preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a string
     */
    public static String getString(Context context, String key, String defaultValue, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * put int preferences
     *
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putInt(Context context, String key, int value, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * get int preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a int
     */
    public static int getInt(Context context, String key, String keyShared) {
        return getInt(context, key, -1,keyShared);
    }

    /**
     * get int preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a int
     */
    public static int getInt(Context context, String key, int defaultValue, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        try {
            return settings.getInt(key, defaultValue);
        }catch (ClassCastException e){
            try {
                return Integer.parseInt(settings.getString(key,defaultValue+""));
            }catch (NumberFormatException en){
                return defaultValue;
            }
        }
    }

    /**
     * put long preferences
     *
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putLong(Context context, String key, long value, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * get long preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a long
     */
    public static long getLong(Context context, String key, String keyShared) {
        return getLong(context, key, -1,keyShared);
    }

    /**
     * get long preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a long
     */
    public static long getLong(Context context, String key, long defaultValue, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        try {
            return settings.getLong(key, defaultValue);
        }catch (ClassCastException e){
            try {
                return Long.parseLong(settings.getString(key,defaultValue+""));
            }catch (NumberFormatException en){
                return defaultValue;
            }
        }
    }

    /**
     * put float preferences
     *
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putFloat(Context context, String key, float value, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a float
     */
    public static float getFloat(Context context, String key, String keyShared) {
        return getFloat(context, key, -1,keyShared);
    }

    /**
     * get float preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a float
     */
    public static float getFloat(Context context, String key, float defaultValue, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        try {
            return settings.getFloat(key, defaultValue);
        }catch (ClassCastException e){
            try {
                return Float.parseFloat(settings.getString(key,defaultValue+""));
            }catch (NumberFormatException en){
                return defaultValue;
            }
        }
    }

    /**
     * put boolean preferences
     *
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putBoolean(Context context, String key, boolean value, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     *         name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, String keyShared) {
        return getBoolean(context, key, false,keyShared);
    }

    /**
     * get boolean preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        try {
            return settings.getBoolean(key, defaultValue);
        }catch (ClassCastException e){
            try {
                return Boolean.parseBoolean(settings.getString(key,defaultValue+""));
            }catch (NumberFormatException en){
                return defaultValue;
            }
        }
    }

    /**
     * put boolean preferences
     *
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference ,  the value of set ,canot be the other class out of java collection
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putStringSet(Context context, String key, Set value, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     *         name that is not a boolean 获取出来的值最终被转换为hashset类型
     */
    public static Set getStringSet(Context context, String key, String keyShared) {
        return getStringSet(context, key,new ArraySet(),keyShared);
    }

    /**
     * get boolean preferences
     *
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a boolean 获取出来的值最终被转换为hashset类型
     */
    public static Set getStringSet(Context context, String key, Set defaultValue, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        return settings.getStringSet(key, defaultValue);
    }
}
