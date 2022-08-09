package com.runt.open.mvvm.retrofit.Interceptor;

import android.util.Log;

import com.google.gson.Gson;
import com.runt.open.mvvm.MyApplication;
import com.runt.open.mvvm.data.PhoneDevice;
import com.runt.open.mvvm.retrofit.net.NetWorkCost;
import com.runt.open.mvvm.retrofit.net.NetWorkListenear;
import com.runt.open.mvvm.retrofit.utils.HttpPrintUtils;
import com.runt.open.mvvm.util.DeviceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
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

    private boolean printLog ;

    public HttpLoggingInterceptor(){
        this(true);
    }
    public HttpLoggingInterceptor(boolean printLog) {
        this.printLog = printLog;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request requestTemp = chain.request();
        int hashCode = requestTemp.hashCode();
        if(printLog) {
            Log.d(TAG, "hashcode:" + hashCode);
        }
        Request.Builder requestBuild = requestTemp.newBuilder()
                .addHeader("device", new Gson().toJson(PhoneDevice.getDevice()))
                .addHeader("appVersion", DeviceUtil.getAppVersionName(MyApplication.getApplication()))
                .addHeader("os", DeviceUtil.isHarmonyOS()? "harmony" : "android");
        Request request = requestBuild.build().newBuilder().build();
        ArrayList<String> logArrays = new ArrayList<>();
        Response response = null;
        try {
            logArrays.addAll(getRequestLog(request));
            int position = logArrays.size() +2;
            //request = encryptRequest(request);//加密
            response = chain.proceed(request);
            logArrays.addAll(getResponseLog(response));
            NetWorkCost netWorkCost = NetWorkListenear.workCostMap.get(hashCode);
            if(netWorkCost != null) {
                logArrays.add(position, "<-- costtimes : " + netWorkCost);
            }
            NetWorkListenear.workCostMap.remove(hashCode);
            new Thread(){
                @Override
                public void run() {
                    if(printLog) {
                        HttpPrintUtils.getInstance().printLog(logArrays, true);//线程安全方法，需在新线程执行，避免阻塞当前线程，导致程序无响应
                    }
                }
            }.start();
        } catch (JSONException e) {
            if(response == null){
                response = chain.proceed(request);
            }
            e.printStackTrace();
        } catch (Exception e) {
            logArrays.add("<-- response url:" + URLDecoder.decode(request.url().toString(), "UTF-8"));
            NetWorkCost netWorkCost = NetWorkListenear.workCostMap.get(hashCode);
            if (netWorkCost != null) {
                netWorkCost.total = new Date().getTime() - netWorkCost.total;
                logArrays.add("<-- costtimes : " + netWorkCost);
            }
            NetWorkListenear.workCostMap.remove(hashCode);
            logArrays.add("<-- response failed " + e.getLocalizedMessage());
            logArrays.add("<--                 " + e.toString());
            new Thread(){
                @Override
                public void run() {
                    if(printLog) {
                        HttpPrintUtils.getInstance().printLog(logArrays, false);//线程安全方法，需在新线程执行，避免阻塞当前线程，导致程序无响应
                    }
                }
            }.start();
            throw e;//抛出异常，用于请求接收信息
        }
        return response;
    }

    private ArrayList<String> getRequestLog(Request request) throws IOException, JSONException {
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
                logArrays.add(new JSONObject(param).toString(4));
            }else if(requestBody instanceof FormBody){
                logArrays.add("---------->REQUEST BODY[FormBody]<----------");
                FormBody body = (FormBody) requestBody;
                for(int i = 0 ; i < body.size() ; i ++ ){
                    param.put(body.name(i),body.value(i));
                }
                logArrays.add(new JSONObject(param).toString(4));
            }else{
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                logArrays.add("---------->REQUEST BODY<----------");
                String str = buffer.readString(charset);
                try{
                    str = URLDecoder.decode(str, "UTF-8");
                }catch (Exception e){}

                if(str.indexOf("[") == 0){
                    logArrays.add(new JSONArray(str).toString(4));
                }else if(str.indexOf("{") == 0){
                    logArrays.add(new JSONObject(str).toString(4));
                }else{
                    logArrays.add(str);
                }
            }
            logArrays.add("--> END " + request.method() + " " + contentType + " ( "
                    + requestBody.contentLength() + "-byte body )");
        }
        return logArrays;

    }


    private ArrayList<String> getResponseLog(Response response) throws IOException, JSONException {
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
                    logArrays.add(new JSONObject(buffer.clone().readString((charset))).toString(4));
                }

                logArrays.add("<-- END HTTP (" + buffer.size() + "-byte body)");
            }

        }
        return logArrays;
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
}
