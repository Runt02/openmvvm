package com.runt.open.mvvm.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.collection.ArraySet;

import java.util.Set;

/**
 * Created by Administrator on 2021/10/28 0028.
 */
public class SpUtils {

    static SpUtils instance;
    
    /**
     * 获取SP实例
     *
     * @return {@link SpUtils}
     */
    public static SpUtils getInstance() {
        if(instance == null){
            instance = new SpUtils();
        }
        return instance;
    }

    
    public final static String PROJECT = "project";
    public final static String USER = "user";


    public boolean clearData(Context context, String keyShared){
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        return settings.edit().clear().commit();
    }


    public boolean clearData(Context context, String key, String keyShared){
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
    public void  removeKey(Context context, String key, String keyShared){

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
    public void  removeValue(Context context, String value, String keyShared){
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
    public boolean putString(Context context, String key, String value, String keyShared) {
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
    public String getString(Context context, String key, String keyShared) {
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
    public String getString(Context context, String key, String defaultValue, String keyShared) {
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
    public boolean putInt(Context context, String key, int value, String keyShared) {
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
    public int getInt(Context context, String key, String keyShared) {
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
    public int getInt(Context context, String key, int defaultValue, String keyShared) {
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
    public boolean putLong(Context context, String key, long value, String keyShared) {
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
    public long getLong(Context context, String key, String keyShared) {
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
    public long getLong(Context context, String key, long defaultValue, String keyShared) {
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
    public boolean putFloat(Context context, String key, float value, String keyShared) {
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
    public float getFloat(Context context, String key, String keyShared) {
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
    public float getFloat(Context context, String key, float defaultValue, String keyShared) {
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
    public boolean putBoolean(Context context, String key, boolean value, String keyShared) {
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
    public boolean getBoolean(Context context, String key, String keyShared) {
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
    public boolean getBoolean(Context context, String key, boolean defaultValue, String keyShared) {
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
    public boolean putStringSet(Context context, String key, Set value, String keyShared) {
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
    public Set getStringSet(Context context, String key, String keyShared) {
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
    public Set getStringSet(Context context, String key, Set defaultValue, String keyShared) {
        SharedPreferences settings = context.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        return settings.getStringSet(key, defaultValue);
    }

    public boolean getBooleanValOfUser(Context context, String key){
        return getBoolean(context,key,false,USER);
    }

    public boolean getBooleanValOfProject(Context context, String key){
        return getBoolean(context,key,false,PROJECT);
    }

    public String getStringValOfUser(Context context, String key){
        return getString(context,key,"",USER);
    }

    public String getStringValOfProject(Context context, String key){
        return getString(context,key,"",PROJECT);
    }

    public int getIntValOfProject(Context context, String key){
        return getInt(context,key,0,PROJECT);
    }

    public Long getLongValOfProject(Context context, String key){
        return getLong(context,key,0,PROJECT);
    }

    public float getFloatValOfProject(Context context, String key){
        return getFloat(context,key,0,PROJECT);
    }

    public Set getStringSetValOfProject(Context context, String key){
        return getStringSet(context,key,PROJECT);
    }

    public int getIntValOfUser(Context context, String key){
        return getInt(context,key,0,USER);
    }

    public Long getLongValOfUser(Context context, String key){
        return getLong(context,key,0,USER);
    }

    public float getFloatValOfUser(Context context, String key){
        return getFloat(context,key,0,USER);
    }

    public Set getStringSetValOfUser(Context context, String key){
        return getStringSet(context,key,USER);
    }


    public void putBooleanValOfUser(Context context, String key ,Boolean value){
        putBoolean(context,key,value,USER);
    }

    public void putBooleanValOfProject(Context context, String key,Boolean value){
        putBoolean(context,key,value,PROJECT);
    }

    public void putStringValOfUser(Context context,String key,String value){
        putString(context,key,value,USER);
    }

    public void putStringValOfProject(Context context,String key,String value){
        putString(context,key,value,PROJECT);
    }

    public void putIntValOfProject(Context context,String key,int value){
        putInt(context,key,value,PROJECT);
    }

    public void putLongValOfProject(Context context,String key,long value){
        putLong(context,key,value,PROJECT);
    }

    public void putFloatValOfProject(Context context,String key,float value){
        putFloat(context,key,value,PROJECT);
    }

    public void putStringSetValOfProject(Context context,String key,Set value){
        putStringSet(context,key,value,PROJECT);
    }

    public void putIntValOfUser(Context context,String key,int value){
        putInt(context,key,value,USER);
    }

    public void putLongValOfUser(Context context,String key,long value){
        putLong(context,key,value,USER);
    }

    public void putFloatValOfUser(Context context,String key,float value){
        putFloat(context,key,value,USER);
    }

    public void putStringSetValOfUser(Context context,String key, Set value){
        putStringSet(context,key,value,USER);
    }


    public void removeUserKey(Context context, String key){
        removeKey(context,key,USER);
    }

    public void removeProjectKey(Context context, String key){
        removeKey(context,key,PROJECT);
    }

    public void removeUserValue(Context context, String Value){
        removeValue(context,Value,USER);
    }

    public void removeProjectValue(Context context, String Value){
        removeValue(context,Value,PROJECT);
    }


    public void clearProjectData(Context context){
        clearData(context,PROJECT);
    }
    public void clearUserData(Context context){
        clearData(context,USER);
    }

}
