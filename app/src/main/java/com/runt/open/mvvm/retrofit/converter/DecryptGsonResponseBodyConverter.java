package com.runt.open.mvvm.retrofit.converter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.util.GsonUtils;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * My father is Object, ites purpose of     解密gson转换器
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-7-22.
 */

public class DecryptGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Charset UTF_8 = Charset.forName("UTF-8");
    private final boolean transHump;//驼峰转换

    public DecryptGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, boolean transHump) {
        this.gson = gson;
        this.adapter = adapter;
        this.transHump = transHump;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = null;
        try {
            String val = new String(value.bytes(),UTF_8);
            Log.e("Converter","val body:"+val);
            response = transHump? GsonUtils.toHumpJson(val):val;
        } catch (JSONException e) {
            e.printStackTrace();
            HttpApiResult apiResult = new HttpApiResult<>();
            apiResult.code = 414;
            apiResult.msg = "非标准json";
            response = new Gson().toJson(apiResult);
        }catch (Exception e){
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            return adapter.read(jsonReader);
        } finally {
            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            JsonReader jsonReader = gson.newJsonReader(new InputStreamReader(inputStream, UTF_8));
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            value.close();
            return result;
        }
    }


}
