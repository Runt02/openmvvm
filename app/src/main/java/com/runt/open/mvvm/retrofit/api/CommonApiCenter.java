package com.runt.open.mvvm.retrofit.api;


import com.runt.open.mvvm.config.Configuration;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.PageResult;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.ui.login.UserBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
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
     * 登录
     * @return
     */
    @POST("loginToken")
    Observable<HttpApiResult<UserBean>> getUserBean();

    @FormUrlEncoded
    @POST
    Observable<HttpApiResult<Results.SmsResult>> getVerifyCode(@Url String url, @Field(Configuration.KEY_PHONE) String phone, @Field(Configuration.KEY_CODE) String code, @Field("time") String time);
    /**
     * app更新
     * @return
     */
    @GET("getControlVersion")
    Observable<HttpApiResult<Results.ApkVersion>> getAppUpdate();

    @FormUrlEncoded
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

    @FormUrlEncoded
    @POST("updateAlipay")
    Observable<Results.StringApiResult> updateAlipay(@Field("account") String account,@Field("paypass") String paypass);

    @FormUrlEncoded
    @POST("updateRealname")
    Observable<Results.StringApiResult> updateRealname(@Field("account") String account,@Field("paypass") String paypass);

    @FormUrlEncoded
    @POST("withDraw")
    Observable<Results.StringApiResult> withDraw(@Field("paypass") String paypass,@Field("count") int count);

    /**
     * 获取签到列表
     * @param month
     * @return
     */
    @GET("getSignsByMonth")
    Observable<HttpApiResult<List<String>>> getSignsByMonth(@Query("month") String month);

    @POST("signIn")
    Observable<Results.StringApiResult> signIn();

    @FormUrlEncoded
    @POST("updatePaypass")
    Observable<Results.StringApiResult> updatePaypass(@Field("smsCode") String smsCode,@Field("paypass") String paypass);
}
