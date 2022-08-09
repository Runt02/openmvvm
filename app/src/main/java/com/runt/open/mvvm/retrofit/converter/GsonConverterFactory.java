package com.runt.open.mvvm.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * My father is Object, ites purpose of     解密gson转换
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-7-22.
 */
public class GsonConverterFactory extends Converter.Factory {

    public static GsonConverterFactory create() {
        return create(new Gson(),false);
    }

    public static GsonConverterFactory create(boolean transHump) {
        return create(new Gson(),transHump);
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
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecryptGsonResponseBodyConverter<>(gson, adapter,transHump);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }
}
