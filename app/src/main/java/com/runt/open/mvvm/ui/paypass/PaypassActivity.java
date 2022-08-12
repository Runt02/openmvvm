package com.runt.open.mvvm.ui.paypass;

import android.app.Instrumentation;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import androidx.recyclerview.widget.GridLayoutManager;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.databinding.ActivityPaypassBinding;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.ui.adapter.NumAdapter;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.widgets.PasswordInputView;

import java.util.ArrayList;
import java.util.List;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-10-30.
 */
public class PaypassActivity extends BaseActivity<ActivityPaypassBinding, PayPassViewModel> {


    @Override
    public void initViews() {
        int type = getIntent().getIntExtra("type",0);// 0 验证 ， 1 修改
        if(type == 0) {
            setTitle("验证支付密码");
        }else{
            setTitle("修改支付密码");
        }
        PasswordInputView password = mBinding.paypassInclude.password;
        mBinding.paypassInclude.view.setOnClickListener(v->{});
        mBinding.paypassInclude.recyclerNum.setLayoutManager(new GridLayoutManager(this,3));
        mBinding.paypassInclude.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() ==6){

                    if(type == 0 ){
                        setResult(RESULT_OK,new Intent().putExtra("paypass",editable.toString()));
                        finish();
                    }else if(type == 1){
                        if(password.getTag() == null){
                            password.setText("");
                            password.setTag(editable.toString());
                            mBinding.paypassInclude.txtTip.setText("请再次输入支付密码");
                        } else if(password.getTag().toString().equals(editable.toString())) {
                            mViewModel.getVerifyCode("getPayPassSMS", UserBean.getUser().getPhone());
                        } else if(!password.getTag().toString().equals(editable.toString())) {
                            password.setText("");
                            password.setTag(null);
                            showToast("两次输入不一致");
                            mBinding.paypassInclude.txtTip.setText("两次输入不一致");
                        }
                    }
                }
            }
        });
        mViewModel.getVerifyResult().observe(this, integer -> {
            if(integer == 0){
                showInputDialog("输入验证码", "", "请输入发送到" + UserBean.getUser().getPhone().substring(8)+"的验证码", new ResPonse() {
                    @Override
                    public void doSuccess(Object obj) {
                        mViewModel.updatePass(password.getText().toString(),obj.toString());
                    }
                });
            }
            password.setText("");
            password.setTag(null);
        });
    }

    @Override
    public void loadData() {
        List<String> list = new ArrayList<>();
        for(int i = 1 ; i < 10; i ++) {
            list.add(i+"");
        }
        list.add("×");
        list.add("0");
        list.add("←");
        NumAdapter numAdapter = new NumAdapter(list);
        numAdapter.setOnItemClickListener((position, s) -> {

            if(s.equals("←")){
                new Thread(() -> {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync( KeyEvent.KEYCODE_DEL  );
                }).start();
            }else if(s.equals("×")){
                showDialog("取消操作", "确定取消当前操作？", new ResPonse() {
                    @Override
                    public void doSuccess(Object obj) {
                        finish();
                    }
                });
            } else {
                new Thread(() -> {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync( Integer.parseInt(s)+7 );
                }).start();
            }
        });
        mBinding.paypassInclude.recyclerNum.setAdapter(numAdapter);
    }
}
