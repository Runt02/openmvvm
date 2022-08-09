package com.runt.open.mvvm.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.runt.open.mvvm.data.HttpApiResult;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * My father is Object, ites purpose of     解密gson转换
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-7-22.
 */
public class GsonConverterFactory extends Converter.Factory {

    public static GsonConverterFactory create() {
        return create(false);
    }

    public static GsonConverterFactory create(boolean transHump) {
        return create(new GsonBuilder().setDateFormat("MMMM dd, yyyy, HH:mm:ss").create(),transHump);
    }


    public static GsonConverterFactory create(Gson gson, boolean transHump) {
        return new GsonConverterFactory(gson,transHump);
    }

    private final Gson gson;

    private final boolean transHump;

    public GsonConverterFactory(Gson gson, boolean transHump) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        this.transHump = transHump;
    }

    @Override
    public Converter<ResponseBody, ? extends HttpApiResult> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<? extends HttpApiResult> adapter = (TypeAdapter<? extends HttpApiResult>) gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(gson, adapter,transHump);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }
}
