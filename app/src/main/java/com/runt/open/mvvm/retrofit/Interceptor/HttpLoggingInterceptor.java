package com.runt.open.mvvm.retrofit.Interceptor;

import android.util.Log;

import com.runt.open.mvvm.retrofit.net.NetWorkCost;
import com.runt.open.mvvm.retrofit.net.NetWorkListenear;
import com.runt.open.mvvm.retrofit.utils.HttpPrintUtils;
import com.runt.open.mvvm.util.GsonUtils;

import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * My father is Object, ites purpose of     log打印
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-21.
 */

public class HttpLoggingInterceptor extends EncryptInterceptor {

    final String TAG = "HttpLogging";

    public HttpLoggingInterceptor() {
    }


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        int hashCode = request.hashCode();
        ArrayList<String> logArrays = getRequestLog(request);
        int position = logArrays.size() +2;
        Response response;
        try {
            //request = encryptRequest(request);//加密
            response = chain.proceed(request);
            logArrays.addAll(getResponseLog(response));
            Log.d(TAG,"hashcode:"+hashCode);
            NetWorkCost netWorkCost = NetWorkListenear.workCostMap.get(hashCode);
            if(netWorkCost != null) {
                String cost = String.format("dns:%s,secure:%s,connect:%s,requestH:%s,requestB:%s,responseH:%s,responseB:%s", convertTimes(netWorkCost.dns), convertTimes(netWorkCost.secure), convertTimes(netWorkCost.connect), convertTimes(netWorkCost.requestHeader), convertTimes(netWorkCost.requestBody), convertTimes(netWorkCost.resposeHeader), convertTimes(netWorkCost.resposeBody));
                logArrays.add(position, "<-- costtimes : " + convertTimes(netWorkCost.total) + " (" + cost + ')');
            }
            NetWorkListenear.workCostMap.remove(hashCode);
            new Thread(){
                @Override
                public void run() {
                    HttpPrintUtils.getInstance().printLog(logArrays, true);//线程安全方法，需在新线程执行，避免阻塞当前线程，导致程序无响应
                }
            }.start();
        } catch (Exception e) {
            logArrays.add("<-- response url:" + URLDecoder.decode(request.url().toString(), "UTF-8"));
            NetWorkCost netWorkCost = NetWorkListenear.workCostMap.get(hashCode);
            String cost = String.format("dns:%s,secure:%s,connect:%s,requestH:%s,requestB:%s,responseH:%s,responseB:%s",  convertTimes(netWorkCost.dns) ,convertTimes(netWorkCost.secure) , convertTimes(netWorkCost.connect),convertTimes(netWorkCost.requestHeader),convertTimes(netWorkCost.requestBody) ,convertTimes(netWorkCost.resposeHeader),convertTimes(netWorkCost.resposeBody)    );
            logArrays.add("<-- costtimes : "+convertTimes(netWorkCost.total)+" (" +cost + ')');
            NetWorkListenear.workCostMap.remove(hashCode);
            logArrays.add("<-- response failed " + e.getLocalizedMessage());
            logArrays.add("<--                 " + e.toString());
            new Thread(){
                @Override
                public void run() {
                    HttpPrintUtils.getInstance().printLog(logArrays, false);//线程安全方法，需在新线程执行，避免阻塞当前线程，导致程序无响应
                }
            }.start();
            throw e;//抛出异常，用于请求接收信息
        }
        return response;
    }

    private ArrayList<String> getRequestLog(Request request) throws IOException {
        RequestBody requestBody = request.body();
        ArrayList<String> logArrays = new ArrayList<>();
        String requestStartMessage = "--> " + request.method() + ' ' + URLDecoder.decode(request.url().toString() ,"UTF-8")+ ' ' ;
        if ( requestBody != null) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logArrays.add(requestStartMessage);
        Headers headers = request.headers();
        logArrays.add("---------->REQUEST HEADER<----------");
        for (int i = 0, count = headers.size(); i < count; i++) {
            logArrays.add(headers.name(i) + ": " + headers.value(i));
        }
        if (requestBody == null) {
            logArrays.add("--> END " + request.method());
        } else if (bodyEncoded(request.headers())) {
            logArrays.add("--> END " + request.method() + " (encoded body omitted)");
        } else {

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            HashMap param = new HashMap();
            if(requestBody instanceof MultipartBody){
                logArrays.add("---------->REQUEST BODY[MultipartBody]<----------");
                MultipartBody body = (MultipartBody) requestBody;
                for(MultipartBody.Part part:body.parts()){
                    Buffer buffer1 = new Buffer();
                    part.body().writeTo(buffer1);
                    String str=buffer1.readString(charset).replaceAll("%(?![0-9a-fA-F]{2})","%25");
                    param.put(part.headers().get(part.headers().name(0)),URLDecoder.decode(str, "UTF-8"));
                }
                logArrays.add(GsonUtils.retractJson(new JSONObject(param).toString()));
            }else if(requestBody instanceof FormBody){
                logArrays.add("---------->REQUEST BODY[FormBody]<----------");
                FormBody body = (FormBody) requestBody;
                for(int i = 0 ; i < body.size() ; i ++ ){
                    param.put(body.name(i),body.value(i));
                }
                logArrays.add(GsonUtils.retractJson(new JSONObject(param).toString()));
            }else{
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                logArrays.add("---------->REQUEST BODY<----------");
                String str = buffer.readString(charset);
                try{
                    logArrays.add(GsonUtils.retractJson(URLDecoder.decode(str, "UTF-8")));
                }catch (Exception e){
                    logArrays.add(str);
                }
            }
            logArrays.add("--> END " + request.method() + " " + contentType + " ( "
                    + requestBody.contentLength() + "-byte body )");
        }
        return logArrays;

    }


    private ArrayList<String> getResponseLog(Response response) throws IOException {
        ArrayList<String> logArrays = new ArrayList<>();
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logArrays.add("<-- response code:" + response.code() + " message:" + response.message()+" contentlength:"+bodySize  );
        logArrays.add("<-- response url:"+URLDecoder.decode(response.request().url().toString(),"UTF-8")  );

        if ( !HttpHeaders.hasBody(response)) {
            logArrays.add("<-- END HTTP");
        } else if (bodyEncoded(response.headers())) {
            logArrays.add("<-- END HTTP (encoded body omitted)");
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
                logArrays.add("---------->RESPONSE BODY<----------");
                if (contentLength != 0) {
                    logArrays.add(retractJson(buffer.clone().readString(charset)));
                }

                logArrays.add("<-- END HTTP (" + buffer.size() + "-byte body)");
            }

        }
        return logArrays;
    }

    /**
     * 字符串缩进
     * @param json
     * @return
     */
    private String retractJson(String json){
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
                    jsonForMatStr.append(c + "\n");
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

    private  String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");//\t或空格
        }
        return levelStr.toString();
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

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private String convertTimes(long ms){
        String m = null,s=null;
        final int utilS = 1000;
        final int utilM = utilS*60;
        if(ms/utilM>0){
            m = ms/utilM+"m";
        }
        if(ms%utilM/utilS>0){
            s = ms%utilM/utilS+"s";
        }
        return (m!=null?m:"")+(s!=null?s:"")+ms%utilS+"ms";
    }

}
