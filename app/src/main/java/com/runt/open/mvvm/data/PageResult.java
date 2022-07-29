package com.runt.open.mvvm.data;

import java.util.ArrayList;

/**
 * Created by Administrator on 2021/10/28 0028.
 */
public class PageResult<T> extends HttpApiResult<String> {
    public int pages;
    public int total;
    public int pageNum;
    public ArrayList<T> rows;

    @Override
    public String toString() {
        return "PageBean{" +
                ", total=" + total +
                ", rows=" + rows +
                '}';
    }
}
