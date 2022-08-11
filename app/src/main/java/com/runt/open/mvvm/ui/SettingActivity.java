package com.runt.open.mvvm.ui;

import android.content.Intent;
import android.view.View;
import com.runt.open.mvvm.BuildConfig;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.ImpViewModel;
import com.runt.open.mvvm.config.Configuration;
import com.runt.open.mvvm.databinding.ActivitySettingBinding;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.ui.login.UserBean;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class SettingActivity extends BaseActivity<ActivitySettingBinding, ImpViewModel> {
    @Override
    public void initViews() {
        setTitle("设置");
        if(UserBean.getUser() == null){
            mBinding.logout.setVisibility(View.GONE);
        }
        mBinding.version.setText(BuildConfig.VERSION_NAME);
        mBinding.version.setOnClickListener(v->{
            mViewModel.checkUpdate(true);
        });
        mBinding.about.setOnClickListener(v->{startActivity(new Intent(mContext,AboutActivity.class));});
        mBinding.logout.setOnClickListener(v->{
            showDialog("是否退出", "退出后将清除本设备的登录信息", new ResPonse() {
                @Override
                public void doSuccess(Object obj) {
                    clearUserData();
                    putStringProjectPrefrence(Configuration.KEY_USERINFO,null);
                    UserBean.setUser(null);
                    setResult(RESULT_CODE_SUCESS);
                    finish();
                }
            });
        });
    }

    @Override
    public void loadData() {

    }
}
