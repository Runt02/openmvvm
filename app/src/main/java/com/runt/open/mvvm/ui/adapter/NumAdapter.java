package com.runt.open.mvvm.ui.adapter;

import com.runt.open.mvvm.base.adapter.BaseAdapter;
import com.runt.open.mvvm.databinding.ItemNumBinding;

import java.util.List;

/**
 * 数字键盘
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-30.
 */
public class NumAdapter extends BaseAdapter<String, ItemNumBinding> {

    public NumAdapter(List<String> list) {
        this.dataList = list;
    }

    @Override
    protected void onBindView(ItemNumBinding binding, int position, String s) {
        binding.text.setText(s);
        binding.getRoot().setTag(s);

    }
}
