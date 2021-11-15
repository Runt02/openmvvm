package com.runt.open.mvvm.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/10/28 0028.
 */
public class BaseApiResult<D extends Object> implements Serializable {
    public String msg;
    public int code = 200;
    public D data;


    @Override
    public String toString() {
        return "BaseApiData{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

}
