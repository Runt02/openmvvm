package com.runt.open.mvvm.retrofit.api;

import com.runt.open.mvvm.config.Configuration;
import com.runt.open.mvvm.data.Results;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public interface LoginApiCenter {
    /**
     * 密码登录
     * @param phone
     * @param pass
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<Results.LoggedInUser> login(@Field(Configuration.KEY_PHONE) String phone, @Field("pass") String pass);

    /**
     * 验证码登录
     * @param phone
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("loginCode")
    Observable<Results.LoggedInUser> loginByCode(@Field(Configuration.KEY_PHONE) String phone, @Field(Configuration.KEY_CODE) String code);


    @FormUrlEncoded
    @POST
    Observable<Results.StringApiResult> getVerifyCode(@Url String url, @Field(Configuration.KEY_PHONE) String phone, @Field(Configuration.KEY_CODE) String code, @Field("time") String time);

    /**
     * 重置密码
     * @param phone
     * @param sms
     * @param newPass
     * @return
     */
    @FormUrlEncoded
    @POST("verifySMSReSetLoginPwd")
    Observable<Results.StringApiResult> resetLoginPwd(@Field(Configuration.KEY_PHONE) String phone,@Field("sms") String sms, @Field("pass") String newPass);


    /**
     * 注册
     * @param phone
     * @param sms
     * @param pass
     * @return
     */
    @FormUrlEncoded
    @POST("registerCustomer")
    Observable<Results.StringApiResult> register(@Field(Configuration.KEY_USERNAME) String phone,@Field("sms") String sms, @Field("pass") String pass);


}
