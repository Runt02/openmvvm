package com.runt.open.mvvm.retrofit.api;


import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.PageResult;
import com.runt.open.mvvm.data.Results;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.*;

import java.util.Map;

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
    @GET("getControlVersion")
    Observable<HttpApiResult<Results.ApkVersion>> getAppUpdate();

    @POST("updateName")
    Observable<Results.StringApiResult> updateName(@Field("username") String name);

    @Multipart
    @POST("updatehead")
    Observable<Results.StringApiResult> updateHead(@Part MultipartBody.Part file);


    /**
     * 获取咨询列表
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @return
     */
    @GET("getMsgList")
    Observable<HttpApiResult<PageResult<Results.Message>>> getMsgList(@Query("page") int pageNum, @Query("size") int pageSize);

    /**
     * app更新
     * @return
     */
    @GET("getMsgDetail")
    Observable<HttpApiResult<Results.Message>> getMsgDetail(@Query("id") String id);

    /**
     * 获取金币记录
     * @param pageNum   页数
     * @param pageSize  每页数量
     * @return
     */
    @GET("coinRecord")
    Observable<HttpApiResult<PageResult<Results.CustomCoin>>> getCoinRecord(@Query("page") int pageNum, @Query("size") int pageSize, @Query("inOrOut") int inOrOut);
}
