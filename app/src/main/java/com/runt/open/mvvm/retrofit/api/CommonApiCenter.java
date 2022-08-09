package com.runt.open.mvvm.retrofit.api;


import com.runt.open.mvvm.data.ApkUpGradeResult;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.PageResult;
import com.runt.open.mvvm.data.Results;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * My father is Object, ites purpose of     常用接口
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-7-21.
 */

public interface CommonApiCenter {

    @GET
    Observable<Object> getData(@Url String url, @QueryMap Map<String,String> param);

    @FormUrlEncoded
    @POST
    Observable<Object> postData(@Url String url, @FieldMap Map<String,String> param);


    /**
     * 分页数据
     * @param url       请求地址
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @param param     其他参数
     * @return
     */
    @GET
    Observable<HttpApiResult<PageResult>> getPageData(@Url String url, @Query("page") int pageNum, @Query("size") int pageSize, @QueryMap Map<String,String> param);

    /**
     * 分页数据
     * @param url       请求地址
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @param param     其他参数
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<Object> postPageData(@Url String url, @Field("page") int pageNum, @Field("size") int pageSize, @FieldMap Map<String,String> param);

    /**
     * app更新
     * @return
     */
    @GET("system/appupgrade/tourist/get/2")
    Observable<ApkUpGradeResult> getAppUpdate();

    @POST("updateName")
    Observable<ApkUpGradeResult> updateName(@Field("username") String name);

    @Multipart
    @POST("updatehead")
    Call<ResponseBody> updateHead(@Part MultipartBody.Part file);


    /**
     * 获取咨询列表
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @return
     */
    @GET("getMsgList")
    Observable<HttpApiResult<PageResult<Results.Message>>> getMsgList(@Query("page") int pageNum, @Query("size") int pageSize);
}
