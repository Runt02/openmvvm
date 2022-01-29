package com.runt.open.mvvm.data;

import android.content.Context;
import android.os.Build;

import com.runt.open.mvvm.util.DeviceUtil;
import com.runt.open.mvvm.util.NetWorkUtils;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-7.
 */

public class PhoneDevice {

    private String brand,model,androidVersion,androidCode,seriaNo,netIp;

    static PhoneDevice device;

    public static void setDevice(Context context) {
        device = new PhoneDevice(Build.BRAND,Build.MODEL,Build.VERSION.SDK_INT+"",Build.VERSION.RELEASE, DeviceUtil.getSerialNumber(context), NetWorkUtils.getNetIp());
    }

    public static PhoneDevice getDevice() {
        return device;
    }

    public PhoneDevice(String brand, String model, String androidVersion, String androidCode, String seriaNo, String netIp) {
        this.brand = brand;
        this.model = model;
        this.androidVersion = androidVersion;
        this.androidCode = androidCode;
        this.seriaNo = seriaNo;
        this.netIp = netIp;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getAndroidCode() {
        return androidCode;
    }

    public void setAndroidCode(String androidCode) {
        this.androidCode = androidCode;
    }

    public String getSeriaNo() {
        return seriaNo;
    }

    public void setSeriaNo(String seriaNo) {
        this.seriaNo = seriaNo;
    }

    public String getNetIp() {
        return netIp;
    }

    public void setNetIp(String netIp) {
        this.netIp = netIp;
    }


    @Override
    public String toString() {
        return "PhoneDevice{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", androidVersion='" + androidVersion + '\'' +
                ", androidCode='" + androidCode + '\'' +
                ", seriaNo='" + seriaNo + '\'' +
                ", netIp='" + netIp + '\'' +
                '}';
    }
}
