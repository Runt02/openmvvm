package com.runt.open.mvvm.retrofit.Interceptor;


import com.runt.open.mvvm.retrofit.utils.RSAUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * My father is Object, ites purpose of     加密拦截器
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-10-8.
 */

public class EncryptInterceptor implements Interceptor {

    protected static final Charset UTF8 = Charset.forName("UTF-8");
    private final String ENCRYPT = "encrypt";

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(encryptRequest(chain.request()));
    }


    //加密
    protected Request encryptRequest(Request request) throws IOException {
        Headers headers = request.headers();
        RequestBody requestBody = request.body();
        Request.Builder builder = request.newBuilder();
        for(int i = 0 ; i < headers.size() ; i ++){
            builder.addHeader(headers.name(i),headers.value(i));
        }
        if(requestBody != null){
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            HashMap param = new HashMap();
            if(requestBody instanceof MultipartBody){
                MultipartBody body = (MultipartBody) requestBody;
                for(MultipartBody.Part part:body.parts()){
                    Buffer buffer1 = new Buffer();
                    part.body().writeTo(buffer1);
                    String str=buffer1.readString(charset).replaceAll("%(?![0-9a-fA-F]{2})","%25");
                    param.put(part.headers().get(part.headers().name(0)), URLDecoder.decode(str, "UTF-8"));
                }
                MultipartBody.Builder mbuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                mbuilder.addFormDataPart(ENCRYPT,encryptParam(param));
                builder.post(mbuilder.build());
            }else if(requestBody instanceof FormBody){
                FormBody body = (FormBody) requestBody;
                for(int i = 0 ; i < body.size() ; i ++ ){
                    param.put(body.name(i),body.value(i));
                }
                FormBody.Builder formBuild = new FormBody.Builder();
                formBuild.add(ENCRYPT,encryptParam(param));
                builder.post(formBuild.build());
            }else{
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String str = buffer.readString(charset);
                String encrypt = encryptJson(str);
                param.put(ENCRYPT,encrypt);
                builder.post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), new JSONObject(param).toString()));
            }
        }
        return builder.build();
    }


    /**
     * 加密传递的参数
     * @param params
     * @return
     */
    public static String encryptParam(Map<String, Object> params){
        return encryptJson(new JSONObject(params).toString());
    }
    public static String encryptJson(String json){
        try {
            return RSAUtils.encrypt(json,RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
