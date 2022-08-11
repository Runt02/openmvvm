package com.runt.open.mvvm.ui.adapter;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.runt.open.mvvm.BuildConfig;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.adapter.BaseAdapter;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.databinding.ItemCoinReportBinding;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.util.HandleDate;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-29.
 */
public class CoinTransAdapter extends BaseAdapter<Results.CustomCoin, ItemCoinReportBinding> {
    @Override
    protected void onBindView(ItemCoinReportBinding binding, int position, Results.CustomCoin data) {

        Context context = binding.getRoot().getContext();
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_head)//图片加载出来前，显示的图片
                .fallback(R.mipmap.default_head) //url为空的时候,显示的图片
                .error(R.mipmap.default_head);//图片加载失败后，显示的图片
        boolean isOut = data.fromUser != null && data.fromUser.getId().equals(UserBean.getUser().getId());
        String type = "";
        switch (data.type) {
            case 0:
                type = "赠送";
                break;
            case 1:
                type = "每日登录";
                break;
            case 2:
                type = "签到赠送";
                break;
            case 3:
                type = "广告赠送";
                break;
            case 4:
                type = "激励广告赠送";
                break;
            case 5:
                type = "转帐";
                break;
            case 6:
                type = "提现";
                break;
            case 7:
                type = "注册赠送";
                break;

        }
        if (isOut) {
            binding.txtName.setText((data.type == 5?"":"转账到-")+(data.toUser == null ? type : data.toUser.getUsername()));
            binding.txtCount.setText("-" + data.count);
            binding.txtCount.setTextColor(context.getResources().getColor(R.color.holo_red_light));
            Glide.with(binding.getRoot().getContext()).load(BuildConfig.HOST_IP_ADDR + (data.toUser == null ? "" : data.toUser.getHead())).apply(options).into(binding.imgHead);
        } else {
            binding.txtName.setText((data.fromUser == null ? type : data.fromUser.getUsername())+(data.type == 5?"-转账":""));
            binding.txtCount.setTextColor(context.getResources().getColor(R.color.green));
            binding.txtCount.setText("+" + data.count);
            Glide.with(binding.getRoot().getContext()).load(BuildConfig.HOST_IP_ADDR + (data.fromUser == null ? "" : data.fromUser.getHead())).apply(options).into(binding.imgHead);
        }
        binding.txtTime.setText(HandleDate.getTimeStateNew(data.cTime));
    }
}
