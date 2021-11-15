package com.runt.open.mvvm.retrofit.net;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * My father is Object, ites purpose of     接口请求耗时监听
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-7-9.
 */

public class NetWorkListenear extends EventListener {

    private static final String TAG = "NetworkEventListener";
    final Charset UTF8 = Charset.forName("UTF-8");
    public static Map<Integer, NetWorkCost> workCostMap = new HashMap<>();

    public static Factory get(){
        Factory factory = new Factory() {
            @NotNull
            @Override
            public EventListener create(@NotNull Call call) {
                return new NetWorkListenear();
            }
        };
        return factory;
    }

    @Override
    public void callStart(@NotNull Call call) {
        super.callStart(call);
        //mRequestId = mNextRequestId.getAndIncrement() + "";
        //getAndAdd，在多线程下使用cas保证原子性
        NetWorkCost netWorkCost = new NetWorkCost();
        netWorkCost.total = new Date().getTime();
        workCostMap.put(call.request().hashCode(),netWorkCost);
    }

    @Override
    public void dnsStart(@NotNull Call call, @NotNull String domainName) {
        super.dnsStart(call, domainName);
        //Log.d(TAG, "dnsStart");
        workCostMap.get(call.request().hashCode()).dns = new Date().getTime();
    }

    @Override
    public void dnsEnd(@NotNull Call call, @NotNull String domainName, @NotNull List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        //Log.d(TAG, "dnsEnd");
        workCostMap.get(call.request().hashCode()).dns = new Date().getTime()  - workCostMap.get(call.request().hashCode()).dns;
    }

    @Override
    public void connectStart(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        //Log.d(TAG, "connectStart");
        workCostMap.get(call.request().hashCode()).connect = new Date().getTime();
    }

    @Override
    public void secureConnectStart(@NotNull Call call) {
        super.secureConnectStart(call);
        //Log.d(TAG, "secureConnectStart");
        workCostMap.get(call.request().hashCode()).secure = new Date().getTime();
    }

    @Override
    public void secureConnectEnd(@NotNull Call call, @Nullable Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        //Log.d(TAG, "secureConnectEnd");
        workCostMap.get(call.request().hashCode()).secure = new Date().getTime() - workCostMap.get(call.request().hashCode()).secure;
    }

    @Override
    public void connectEnd(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress,
                           @NotNull Proxy proxy, @Nullable Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        //Log.d(TAG, "connectEnd");
        workCostMap.get(call.request().hashCode()).connect = new Date().getTime() - workCostMap.get(call.request().hashCode()).connect;
    }

    @Override
    public void connectFailed(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy, @Nullable Protocol protocol, @NotNull IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        workCostMap.get(call.request().hashCode()).connect = new Date().getTime() - workCostMap.get(call.request().hashCode()).connect;
        workCostMap.get(call.request().hashCode()).total = new Date().getTime() - workCostMap.get(call.request().hashCode()).total;
        //Log.d(TAG, "connectFailed");
    }

    @Override
    public void requestHeadersStart(@NotNull Call call) {
        super.requestHeadersStart(call);
        //Log.d(TAG, "requestHeadersStart");
        workCostMap.get(call.request().hashCode()).requestHeader = new Date().getTime();
    }

    @Override
    public void requestHeadersEnd(@NotNull Call call, @NotNull Request request) {
        super.requestHeadersEnd(call, request);
        //Log.d(TAG, "requestHeadersEnd");
        workCostMap.get(call.request().hashCode()).requestHeader = new Date().getTime() - workCostMap.get(call.request().hashCode()).requestHeader;
    }

    @Override
    public void requestBodyStart(@NotNull Call call) {
        super.requestBodyStart(call);
        //Log.d(TAG, "requestBodyStart");
        workCostMap.get(call.request().hashCode()).requestBody = new Date().getTime();
    }

    @Override
    public void requestBodyEnd(@NotNull Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        //Log.d(TAG, "requestBodyEnd");
        workCostMap.get(call.request().hashCode()).requestBody = new Date().getTime() - workCostMap.get(call.request().hashCode()).requestBody;
    }

    @Override
    public void responseHeadersStart(@NotNull Call call) {
        super.responseHeadersStart(call);
        //Log.d(TAG, "responseHeadersStart");
        workCostMap.get(call.request().hashCode()).resposeHeader = new Date().getTime();
    }

    @Override
    public void responseHeadersEnd(@NotNull Call call, @NotNull Response response) {
        super.responseHeadersEnd(call, response);
        //Log.d(TAG, "responseHeadersEnd");
        workCostMap.get(call.request().hashCode()).resposeHeader = new Date().getTime() - workCostMap.get(call.request().hashCode()).resposeHeader;
    }

    @Override
    public void responseBodyStart(@NotNull Call call) {
        super.responseBodyStart(call);
        //Log.d(TAG, "responseBodyStart");
        workCostMap.get(call.request().hashCode()).resposeBody = new Date().getTime();
    }

    @Override
    public void responseBodyEnd(@NotNull Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        //Log.d(TAG, "responseBodyEnd");
        workCostMap.get(call.request().hashCode()).resposeBody = new Date().getTime() - workCostMap.get(call.request().hashCode()).resposeBody;
        workCostMap.get(call.request().hashCode()).total = new Date().getTime() - workCostMap.get(call.request().hashCode()).total;
    }


    @Override
    public void callFailed(@NotNull Call call, @NotNull IOException ioe) {
        super.callFailed(call, ioe);
        workCostMap.get(call.request().hashCode()).total = new Date().getTime() - workCostMap.get(call.request().hashCode()).total;
        //Log.d(TAG, "callFailed");
    }

}
