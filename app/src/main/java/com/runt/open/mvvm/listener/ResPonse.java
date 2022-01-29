package com.runt.open.mvvm.listener;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/25.
 */
public abstract class ResPonse<T> implements Serializable {

    public abstract void doSuccess(T obj);

    public  void doError(T obj){};

}
