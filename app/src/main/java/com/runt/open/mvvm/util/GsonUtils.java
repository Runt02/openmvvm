/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.runt.open.mvvm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Json工具类.
 */
public class GsonUtils {
    private static Gson gson = new GsonBuilder().create();

    public static String toJson(Object value) {
        return gson.toJson(value);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonParseException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonParseException {
        return (T) gson.fromJson(json, typeOfT);
    }

    /**
     * 将对象转换为驼峰命名的json
     * @param value
     * @return
     */
    public static String toHumpJson(Object value) {
        try {
            if(value instanceof Collection){
                    return convertToHumpJsonArray(new JSONArray(gson.toJson(value)) ).toString();
            }else {
                return convertToHumpJsonObj(new JSONObject(gson.toJson(value)) ).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return gson.toJson(value);
        }
    }

    /**
     *
     * 将json转换为驼峰命名的json
     * @param json
     * @return
     */
    public static String toHumpJson(String json) throws JSONException {
        if(json.indexOf("[") == 0){
            return convertToHumpJsonArray(new JSONArray(json) ).toString();
        }else {
            return convertToHumpJsonObj(new JSONObject(json) ).toString();
        }
    }

    /**
     * 驼峰命名转换
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     * @throws JsonParseException
     */
    public static <T> T fromJsonToHump(String json, Class<T> classOfT) throws JsonParseException, JSONException {
        return  gson.fromJson(toHumpJson(json), classOfT);
    }

    /**
     * 驼峰命名转换
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     * @throws JsonParseException
     */
    public static <T> T fromJsonToHump(String json, Type typeOfT) throws JsonParseException, JSONException {
        return (T) gson.fromJson(toHumpJson(json), typeOfT);
    }

    /**
     * 转换驼峰命名
     * @param jsonObject
     * @return
     */
    public static JSONObject convertToHumpJsonObj(JSONObject jsonObject){
        JSONObject temp = new JSONObject();
        Iterator<String> it = jsonObject.keys();
        while ( it.hasNext()){
            String key = it.next();
            String humpKey = humpName(key);
            try {
                if(jsonObject.get(key) instanceof JSONObject){
                    temp.put(humpKey,convertToHumpJsonObj(jsonObject.getJSONObject(key)));
                }else if(jsonObject.get(key) instanceof JSONArray){
                    temp.put(humpKey,convertToHumpJsonArray(jsonObject.getJSONArray(key)));
                }else {
                    temp.put(humpKey,jsonObject.get(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;

    }

    public static JSONArray convertToHumpJsonArray(JSONArray array){
        JSONArray jsons = new JSONArray();
        for(int i = 0 ; i < array.length() ; i ++){
            try {
                if(array.get(i) instanceof JSONObject){
                    jsons.put(convertToHumpJsonObj(array.getJSONObject(i)));
                }else if(array.get(i) instanceof JSONArray){
                    jsons.put(convertToHumpJsonArray(array.getJSONArray(i)));
                }else {
                    jsons.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsons;

    }
    /**
     * 将key转换为驼峰
     * @param param
     * @return
     */
    public static Map convertToHumpMap(Map<String, Object> param){
        Map temp = new TreeMap();
        for(String key: param.keySet()){
            String humpKey = humpName(key);
            if(param.get(key) instanceof Map){
                temp.put(humpKey,convertToHumpMap((Map<String, Object>) param.get(key)));
            }else if(param.get(key) instanceof List){
                temp.put(humpKey,convertToHumpList((List)param.get(key)));
            }else {
                temp.put(humpKey,param.get(key));
            }
        }
        return temp;
    }


    public static List convertToHumpList(List list){
        List ars = new ArrayList();
        for(Object object : list){
            if(object instanceof Map){
                ars.add(convertToHumpMap((Map)object));
            }else if(object instanceof List){
                ars.add(convertToHumpList((List)object));
            }else {
                ars.add(object);
            }
        }
        return ars;
    }
    /**
     * 驼峰命名
     * @param name
     * @return
     */
    public static String humpName(String name){
        String[] strings = name.split("_");
        StringBuilder sb = new StringBuilder();
        sb.append(strings[0]);
        for(int i = 1 ; i < strings.length ; i ++){
            sb.append(toUperFirst(strings[i]));
        }
        if(sb.toString().equals("new")){//关键字 转成大写
            return "NEW";
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String toUperFirst(String name){
        return name.substring(0,1).toUpperCase()+name.substring(1);
    }

    /**
     * json字符串缩进
     * @param json
     * @return
     */
    public static String retractJson(String json){
        int level = 0 ;
        StringBuffer jsonForMatStr = new StringBuffer();
        for(int index=0;index<json.length();index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = json.charAt(index);
            //          System.out.println(s.charAt(index));

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
                //                System.out.println("123"+jsonForMatStr);
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    if(index>0 && index < json.length()-2 && (json.charAt(index-1) != '\n') && json.charAt(index+1) == '"'){
                        jsonForMatStr.append(c + "\n");
                    }else{
                        jsonForMatStr.append(c);
                    }
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }
    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");//\t或空格
        }
        return levelStr.toString();
    }
}
