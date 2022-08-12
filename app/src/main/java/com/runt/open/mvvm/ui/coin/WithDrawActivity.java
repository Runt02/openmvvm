package com.runt.open.mvvm.ui.coin;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.databinding.ActivityWithdrawBinding;
import com.runt.open.mvvm.listener.CustomClickListener;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.ui.paypass.PaypassActivity;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-30.
 */
public class WithDrawActivity extends BaseActivity<ActivityWithdrawBinding, CoinViewModel> {

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_OK){
            int count = Integer.parseInt(mBinding.edit.getText().toString())*1000;
            mViewModel.withDraw(result.getData().getStringExtra("paypass"),count);
        }
    });

    @Override
    public void initViews() {

        mBinding.txtBalance.setText(String.format("当前金币数量%s个", UserBean.getUser().getCoin()));
        mBinding.edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.txtRmb.setText(String.format("提现￥%s元",mBinding.edit.getText()));
            }
        });
        mBinding.btnSubmit.setOnClickListener(new CustomClickListener() {
            @Override
            protected void onSingleClick(View view) {
                if(isNull(mBinding.edit.getText())){
                    showToast("请输入提现数量");
                }else{
                    int count = Integer.parseInt(mBinding.edit.getText().toString())*1000;
                    if(count>UserBean.getUser().getCoin()){
                        showToast("余额不足");
                    }else if( isNull(UserBean.getUser().getAlipay())){
                        showToast("还没有设置支付宝账号");
                    }else {
                        launcher.launch(new Intent(mContext, PaypassActivity.class));//开启支付密码
                    }
                }
            }
        });
    }

    @Override
    public void loadData() {

    }
}
