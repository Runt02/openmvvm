package com.runt.open.mvvm.ui.coin;

import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.databinding.ActivitySettingCoinBinding;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.ui.paypass.PaypassActivity;

/**
 * My father is Object, ites purpose of 金币交易设置
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-30.
 */
public class CoinSettingActivity extends BaseActivity<ActivitySettingCoinBinding, CoinViewModel> {

    private int requestCode = 0;

    @Override
    public void initViews() {
        ActivityResultLauncher<Intent>  launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if( result.getResultCode() == RESULT_OK){
                String pass = result.getData().getStringExtra("paypass");
                HttpObserver<String> observer = new HttpObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        if(REQUEST_CODE_PAYPASS_FOR_ALIPAY == requestCode ){
                            showToast("支付宝修改成功");
                            UserBean.getUser().setAlipay(mBinding.txtAlipay.getText().toString());
                        }else if(REQUEST_CODE_PAYPASS_FOR_REALNAME == requestCode ){
                            showToast("修改成功");
                            UserBean.getUser().setRealname(mBinding.txtRealname.getText().toString());
                        }
                    }

                    @Override
                    protected void onFailed(HttpApiResult error) {
                        super.onFailed(error);
                        loadData();
                        if(error.code == 621){
                            showDialog("设置支付密码", "当前账号还没有设置支付密码", "设置", "取消", new ResPonse() {
                                @Override
                                public void doSuccess(Object obj) {
                                    startActivity(new Intent(mContext, PaypassActivity.class).putExtra("type", 1));//去设置密码
                                }
                            });
                        }

                    }
                };
                if(REQUEST_CODE_PAYPASS_FOR_ALIPAY == requestCode ){
                    mViewModel.updateAlipay(mBinding.txtAlipay.getText().toString(),pass,observer);
                }else if(REQUEST_CODE_PAYPASS_FOR_REALNAME == requestCode ){
                    mViewModel.updateName(mBinding.txtRealname.getText().toString(),pass,observer);
                }
            }
        });
        mBinding.linAlipay.setOnClickListener(v->{
            showInputDialog("修改支付宝账号", UserBean.getUser().getAlipay(), "请输入支付宝账号用于提现", new ResPonse() {
                @Override
                public void doSuccess(Object obj) {
                    mBinding.txtAlipay.setText(obj.toString());
                    requestCode = REQUEST_CODE_PAYPASS_FOR_ALIPAY;
                    launcher.launch(new Intent(mContext, PaypassActivity.class));
                }
            });
        });
        mBinding.linRealname.setOnClickListener(v->{
            showInputDialog("修改真实姓名", UserBean.getUser().getRealname(), "请输入真实姓名用于提现", new ResPonse() {
                @Override
                public void doSuccess(Object obj) {
                    requestCode = REQUEST_CODE_PAYPASS_FOR_REALNAME;
                    mBinding.txtRealname.setText(obj.toString());
                    launcher.launch(new Intent(mContext, PaypassActivity.class));
                }
            });
        });
        mBinding.linPass.setOnClickListener(v->{
            startActivity(new Intent(mContext,PaypassActivity.class).putExtra("type",1));
        });
    }

    @Override
    public void loadData() {
        if(UserBean.getUser().getAlipay()!=null){
            mBinding.txtAlipay.setText(UserBean.getUser().getAlipay());
        }else{
            mBinding.txtAlipay.setText("");
        }
        if(UserBean.getUser().getRealname() != null){
            mBinding.txtRealname.setText(UserBean.getUser().getRealname());
        }else{
            mBinding.txtRealname.setText("");
        }
    }
}
