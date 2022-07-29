package com.runt.open.mvvm.retrofit.net;


/**
 * My father is Object, ites purpose of 网络消耗
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2021-3-12.
 */

public class NetWorkCost {

    //网络消耗时间
    public long dns,connect,total,secure,requestHeader,requestBody,resposeHeader,resposeBody;

    @Override
    public String toString() {
        return "NetWorkCost{" +
                "total=" + convertTimes(total) +
                ", dns=" + convertTimes(dns) +
                ", connect=" + convertTimes(connect) +
                ", secure=" + convertTimes(secure) +
                ", requestHeader=" + convertTimes(requestHeader) +
                ", requestBody=" + convertTimes(requestBody) +
                ", resposeHeader=" + convertTimes(resposeHeader) +
                ", resposeBody=" + convertTimes(resposeBody) +
                '}';
    }

    private String convertTimes(long ms){
        String m = null,s=null;
        final int utilS = 1000;
        final int utilM = utilS*60;
        if(ms/utilM>0){
            m = ms/utilM+"m";
        }
        if(ms%utilM/utilS>0){
            s = ms%utilM/utilS+"s";
        }
        return (m!=null?m:"")+(s!=null?s:"")+ms%utilS+"ms";
    }
}
