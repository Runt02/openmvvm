package com.runt.open.mvvm.ui.msg;

import android.webkit.WebSettings;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.databinding.ActivityMsgDetailBinding;
import com.runt.open.mvvm.util.HandleDate;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-29.
 */
public class MsgDetailActivity extends BaseActivity<ActivityMsgDetailBinding,MsgDetailViewModel> {
    @Override
    public void initViews() {
        mViewModel.detailLive.observe(this, message -> {
            mBinding.txtMsgTitle.setText(message.title);
            mBinding.txtAuthor.setText(message.cUName);
            mBinding.txtTime.setText(HandleDate.getTimeStateNew(HandleDate.getDateTimeToLong(message.cTime))+" · "+getString(R.string.created_at));
            WebSettings settings = mBinding.txtContent.getSettings();
            settings.setTextZoom(80); // 通过百分比来设置文字的大小，默认值是100。
            settings.setDefaultTextEncodingName("UTF-8");
            mBinding.txtContent.loadData(message.content,"text/html","UTF-8");
        });
    }

    @Override
    public void loadData() {
        mViewModel.getMsgDetail(getIntent().getStringExtra("id"));
    }
}
