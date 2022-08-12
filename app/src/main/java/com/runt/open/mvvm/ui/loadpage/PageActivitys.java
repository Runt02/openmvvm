package com.runt.open.mvvm.ui.loadpage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.activities.LoadPageActivity;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.databinding.ActivityRecyclerBinding;
import com.runt.open.mvvm.listener.CustomClickListener;
import com.runt.open.mvvm.ui.adapter.CoinTransAdapter;
import com.runt.open.mvvm.ui.coin.CoinSettingActivity;

import java.util.Map;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class PageActivitys {

    //金币记录
    public static class CoinRecordActivity extends LoadPageActivity<ActivityRecyclerBinding, PageViewModels.CoinRecordViewModel, CoinTransAdapter, Results.CustomCoin>{
        @Override
        protected String initTitle() {
            return "金币记录";
        }

        @Override
        public void initViews() {
            super.initViews();
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_white_setting);
            drawable.setTint(getResources().getColor(R.color.txt_color));
            setTitleRight(drawable);
            titleBarView.setRightClick(new CustomClickListener() {
                @Override
                protected void onSingleClick(View view) {
                    startActivity(new Intent(mContext, CoinSettingActivity.class));//打开设置
                }
            });
        }

        @Override
        protected Map requestParams() {
            Map map = super.requestParams();
            map.put("inOrOut",0);
            return map;
        }
    }

}
