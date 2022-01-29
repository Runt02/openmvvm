package com.runt.open.mvvm.data;

/**
 * My father is Object, ites purpose of 
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2022/1/28.
 */
public class LoadingCmd {
    public CMD code;public String msg;

    public LoadingCmd(CMD code) {
        this(code,"");
    }

    public LoadingCmd(CMD code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public enum CMD{LOADING,DISSMISS}
}
