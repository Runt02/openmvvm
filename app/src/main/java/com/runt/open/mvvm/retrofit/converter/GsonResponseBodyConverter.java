package com.runt.open.mvvm.retrofit.converter;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.util.GsonUtils;
import okhttp3.ResponseBody;
import org.json.JSONException;
import retrofit2.Converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * My father is Object, ites purpose of     解密gson转换器
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-7-22.
 */

public class GsonResponseBodyConverter<T extends HttpApiResult> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Charset UTF_8 = Charset.forName("UTF-8");
    private final boolean transHump;//驼峰转换

    public GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, boolean transHump) {
        this.gson = gson;
        this.adapter = adapter;
        this.transHump = transHump;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        T result = null;
        String response = null;
        try {
            String val = new String(value.bytes(),UTF_8);
            Log.e("Converter","val body:"+val);
            response = transHump? GsonUtils.toHumpJson(val):val;
            result = readString(response);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e("Converter","Throwable 数据类型转换错误 "+e);
            HttpApiResult apiResult = new HttpApiResult<>();
            apiResult.data = response;
            if(e instanceof JSONException){
                Log.e("Converter","Throwable 非标准json "+e);
                apiResult.code = 1014;
                apiResult.msg = "非标准json";
            }else if(e instanceof JsonSyntaxException){
                Log.e("Converter","Throwable 数据类型转换错误 "+e);
                apiResult.code = 1015;
                apiResult.msg = "数据类型转换错误";
            }else {
                apiResult.code = 1016;
                apiResult.msg = "类型转换错误"+e.getMessage();
                Log.e("Converter","Throwable "+e);
            }
            response = new Gson().toJson(apiResult);
            result = readString(response);
        } finally {
            value.close();
            return result;
        }
    }

    private T readString(String str) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(inputStream, UTF_8));
        T result = adapter.read(jsonReader);
        if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
            throw new JsonIOException("JSON document was not fully consumed.");
        }
        return result;
    }

}
