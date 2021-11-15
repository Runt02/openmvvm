package com.runt.open.mvvm.retrofit.api;

import com.runt.open.mvvm.ui.login.LoggedInUser;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public interface LoginApiCenter {
    @FormUrlEncoded
    @POST("login")
    Observable<LoggedInUser> login(@Field("phone") String phone,@Field("pass") String pass);


    @FormUrlEncoded
    @POST("login")
    Observable<LoggedInUser> loginByCode(@Field("phone") String phone,@Field("code") String code);


}
