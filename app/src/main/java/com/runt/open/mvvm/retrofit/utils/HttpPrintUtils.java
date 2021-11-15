package com.runt.open.mvvm.retrofit.utils;

import com.runt.open.mvvm.BuildConfig;
import com.runt.open.mvvm.util.GsonUtils;
import com.runt.open.mvvm.util.MyLog;

import java.io.EOFException;
import java.util.ArrayList;

import okio.Buffer;

/**
 * My father is Object, ites purpose of 单例模式  保证synchronized方法的线程安全性
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-5-13.
 */

public class HttpPrintUtils {
    String TAG = "HttpPrintUtils";
    static HttpPrintUtils instance;
    public static  HttpPrintUtils getInstance(){
        if(instance == null){
           instance = new HttpPrintUtils();
        }
        return instance;
    }

    /**
     * 打印log
     * @param list
     */
    public synchronized void printLog(ArrayList<String> list, boolean info){
        int length = 0 ;//计算每行最长的长度
        ArrayList<String> logArrays = new ArrayList<>();
        for(String str : list){
            if(str.indexOf("\n")>-1){//有换行的拆分处理
                String[] split = str.split("\n");
                for(String s : split){
                    s = s.replace("\t","    ");//缩进替换空格
                    if(length<s.length()){
                        length = s.length();
                    }
                }
            }else{
                if(length<str.length()){
                    length = str.length();
                }
            }
        }
        length+=14;//左右间距
        if(length>300){
            length = 300;
        }
        String head = "HTTP  REQUEST START";
        logArrays.add(" \n\n\n"+"\n");
        //打印头部
        String logHead = "┏"+getEmptyStr((length-head.length())/2,"━")+head+getEmptyStr((length-head.length())/2,"━")+"┓";
        logArrays.add(logHead+"\n");
        //打印内容
        for(String str : list){
            String logStr = "";
            if(str.indexOf("\n")>-1){//内部换行替换
                splitStr(str,logHead.length(),logArrays);
            }else{
                if(str.length()> logHead.length()){
                    outOflength(str,logHead.length(),logArrays);
                }else {
                    logStr = "┃      " + str + getEmptyStr((length - 14 - str.length()), " ");
                    //处理中文间距，保证打印无偏差
                    logArrays.add(logStr + getEmptyStr((logHead.length() - logStr.length() - 1 - hasCNchar(logStr)), " ") + "┃ \n");
                }
            }
        }
        String end = "HTTP  REQUEST END";
        //打印结尾
        logArrays.add("┗"+getEmptyStr((length-end.length())/2,"━")+end+getEmptyStr((length-end.length())/2,"━")+"┛\n");
        logArrays.add(" \n\n\n");
        //Logger.DEFAULT.log(sb.toString());//打印log，避免多个log语句，导致log输出时其他线程的log输出切入此输出阵列内
        if(BuildConfig.DEBUG) {
            for(int i = 0 ; i < logArrays.size() ; i ++ ){
                String str = logArrays.get(i);
                if (info) {
                    MyLog.i(TAG , str.replace("\n","")+" "+logArrays.size()+" "+i);
                } else {
                    MyLog.e(TAG , str.replace("\n","")+" "+logArrays.size()+" "+i);
                }
            }
        }
    }

    /**
     * 拆分
     * @param str
     * @param totalLength
     * @param list
     */
    private void splitStr(String str,int totalLength,ArrayList<String> list){
        String logStr = "";
        String[] split = str.split("\n");
        for(String s : split){
            if(s.length()/totalLength>3){
                s = s.substring(0,totalLength*3)+"...";
            }
            s = s.replace("\t","    ");//缩进替换空格
            if(s.indexOf("\":{\"")>-1 || s.indexOf("\":[{\"")>-1 || s.indexOf("\":[[")>-1){//内容非校正缩进，且为json字符规范
                splitStr(s.substring(0,s.indexOf("\":")+2)+ GsonUtils.retractJson(s.substring(s.indexOf("\":")+2)),totalLength,list);
            }else {
                if(s.length()> totalLength){
                    outOflength(s,totalLength,list);
                }else {
                    logStr = "┃      " + s + getEmptyStr((totalLength - 16 - s.length()), " ");
                    //处理中文间距，保证打印无偏差
                    list.add(logStr + getEmptyStr((totalLength - logStr.length() - 1 - hasCNchar(logStr)), " ") + "┃ "/*+logStr.length()+" "+logStr.getBytes().length+" "+(" ").getBytes().length +" "+hasCNchar(s)*/ + "\n");
                }
            }
        }
    }


    /**
     * 超长字符拆分
     * @param str
     * @param total
     * @param list
     */
    private void outOflength(String str,int total,ArrayList<String> list){
        String logStr = "";
        //缩进空间
        String space  = getEmptyStr(str.length() - str.trim().length()+4," ");
        //要拆分的实际长度
        int length = (str.length()-space.length());
        //每行数量
        int count = total-16-space.length();//总长度-间距-缩进空间是每行的数量
        //最终拆分数量
        int lines = (length/count) + (length%(count)>0?1:0);

        for(int i = 0 ; i < lines ; i ++){
            int start = space.length() + (count * (i+1));//起始位
            int end = start+count;//结束位
            String s = "";
            if(start > str.length() && i > 0){
                break;
            } else if(end > str.length() && i > 0 || i == lines-1){
                s = str.substring(start);
            } else if(i == 0 ){
                s = str.substring(0, start);
            } else {
                s = str.substring(start, end);
            }
            if(i>0) {
                s = space + s;
            }
            logStr = "┃      " + s + getEmptyStr((total - 16 - s.length()), " ");
            list.add(logStr + getEmptyStr((total - logStr.length() - 1 - hasCNchar(logStr)), " ") + "┃ \n");
        }
    }


    //返回包含中文数量，
    private int hasCNchar(String str){
        str = str.replace("┃","");
        int size = 0 ;
        for(int i = 0 ; i < str.length() ; i ++){
            char c = str.charAt(i);

            if((c >= 0x0391 && c <= 0xFFE5)) { //中文字符
                size++;
            }
        }
        return size>0?(int)(size/3.0*2):0;//+1为修正在log中与英文字符短一位
    }

    /**
     * 占位符填充
     * @param length 占位数量
     * @param space 占位符
     * @return
     */
    private String getEmptyStr(int length,String space){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < length  ; i ++){
            sb.append(space);
        }
        return sb.toString();
    }
    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
