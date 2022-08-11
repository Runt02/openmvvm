package com.runt.open.mvvm.ui;

import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.ImpViewModel;
import com.runt.open.mvvm.databinding.ActivityAboutBinding;
import com.runt.open.mvvm.util.DeviceUtil;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding, ImpViewModel> {
    @Override
    public void initViews() {
        mBinding.txtVer.setText("版本号 "+ DeviceUtil.getAppVersionName(mContext));
    }

    @Override
    public void loadData() {

    }
}
