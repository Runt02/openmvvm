package com.runt.open.mvvm.ui.main.home;

import android.view.View;

import com.runt.open.mvvm.base.adapter.BaseAdapter;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.databinding.ItemMsgBinding;
import com.runt.open.mvvm.listener.CustomClickListener;
import com.runt.open.mvvm.util.HandleDate;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-8-21.
 */

public class MsgAdapter extends BaseAdapter<Results.Message, ItemMsgBinding> {

    @Override
    protected void onBindView(ItemMsgBinding binding, int position, Results.Message message) {
        binding.txtDetail.setText(message.content);
        binding.txtTime.setText(HandleDate.getTimeStateNew(HandleDate.getDateTimeToLong(message.cTime)));
        binding.txtTitle.setText(message.title);
        binding.getRoot().setOnClickListener(new CustomClickListener() {
            @Override
            protected void onSingleClick(View view) {
                //context.startActivity(new Intent(context, MsgDetailActivity.class).putExtra("id", data.get("id").toString()));
            }
        });
    }
}
