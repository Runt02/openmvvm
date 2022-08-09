package com.runt.open.mvvm.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.config.Configuration;
import com.runt.open.mvvm.databinding.ActivityLoginBinding;
import com.runt.open.mvvm.listener.CustomClickListener;
import com.runt.open.mvvm.ui.web.WebViewActivity;
import com.runt.open.mvvm.util.AlgorithmUtils;
import com.runt.open.mvvm.util.MyLog;
import com.runt.open.mvvm.util.PhoneUtil;

import java.util.Date;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2022/1/29.
 */
public class RegisterLoginActivity extends BaseActivity<ActivityLoginBinding,LoginViewModel> {
    final String VERIFY_CODE = "verify_code";
    final String TAG = "RegisterLoginActivity";
    int type = 0;//0 登录,1忘记密码,2注册，-1短信登录

    @Override
    public void initViews() {
        mBinding.txtGetVerify.setOnClickListener(onclick);
        mBinding.txtForgot.setOnClickListener(onclick);
        mBinding.txtLogin.setOnClickListener(onclick);
        mBinding.txtRegister.setOnClickListener(onclick);
        mBinding.txtPrivacy.setOnClickListener(onclick);
        mBinding.button.setOnClickListener(onclick);
        mBinding.editPhone.setText(getStringProjectPrefrence(Configuration.KEY_USERNAME));
        mViewModel.getVerifyResult().observe(this, result -> {
           if(result == 0){
               putLongProjectPrefrence(VERIFY_CODE,new Date().getTime());
               CodeTimer codeTimer = new CodeTimer(60000, 1000, mBinding.txtGetVerify);
               codeTimer.startUp();
           }else{

           }
        });
        mViewModel.getLoginResult().observe(this, loggedInUser -> {
            putBooleanProjectPrefrence(Configuration.IS_LOGIN,true);
            putStringProjectPrefrence(Configuration.KEY_USERNAME, mBinding.editPhone.getText().toString());

            UserBean user = new Gson().fromJson(new Gson().toJson(loggedInUser) ,UserBean.class);
            UserBean.setUser(user);
            putStringProjectPrefrence(Configuration.KEY_TOKEN, user.getToken());
            MyLog.i("registerlogin",user.toString());
            showToast(R.string.login_success);
            setResult(RESULT_CODE_SUCESS);
            finish();
        });
    }

    @Override
    public void loadData() {
        long getTime = getLongProjectPrefrence(VERIFY_CODE);
        long cha = new Date().getTime() - getTime;
        if(cha <1000*60){
            CodeTimer codeTimer = new CodeTimer((60) * 1000-cha, 1000, mBinding.txtGetVerify);
            codeTimer.startUp();
        }
        changeView();
    }

    CustomClickListener onclick = new CustomClickListener() {
        @Override
        protected void onSingleClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    submit();
                    break;
                case R.id.txt_get_verify:

                    String phone = mBinding.editPhone.getText().toString();
                    if(!verifyPhone(phone)){//验证手机
                        return;
                    }
                    if(type==2){//获取注册验证码
                        mViewModel.getRegisterSMS(phone);
                    }else if(type ==1){
                        mViewModel.getForgetSMS( phone);
                    }else if(type == -1){
                        mViewModel.getLoginSMS( phone);
                    }
                    break;
                case R.id.txt_privacy:
                    startActivity(new Intent(mContext, WebViewActivity.class).putExtra(PARAMS_URL,"http://www.hefan.space/privacyPolicy.html").putExtra(PARAMS_TITLE,"隐私政策"));
                    break;
                case R.id.txt_register:
                    type = 2;
                    changeView();
                    break;
                case R.id.txt_forgot:
                    type = 1;
                    changeView();
                    break;
                case R.id.txt_login:
                    if(type != 0 ){
                        type = 0;
                    }else  {
                        type = -1;
                    }
                    changeView();
                    break;
            }
        }
    };


    /**
     * 修改页面布局
     */
    private void changeView(){
        mBinding.button.setEnabled(true);
        mBinding.txtRegister.setVisibility(View.VISIBLE);
        mBinding.checkbox.setVisibility(View.GONE);
        mBinding.txtPrivacy.setVisibility(View.GONE);
        switch (type){
            case -1://短信登录
                mBinding.editVerifyCode.setVisibility(View.VISIBLE);
                mBinding.txtGetVerify.setVisibility(View.VISIBLE);
                mBinding.editPass.setVisibility(View.GONE);
                mBinding.editPassRepeat.setVisibility(View.GONE);
                mBinding.txtForgot.setVisibility(View.VISIBLE);
                mBinding.txtLogin.setText(getResources().getString(R.string.login));
                mBinding.button.setText(getResources().getString(R.string.login));
                mBinding.checkbox.setVisibility(View.VISIBLE);
                mBinding.txtPrivacy.setVisibility(View.VISIBLE);
                break;
            case 0://登录
                mBinding.editVerifyCode.setVisibility(View.GONE);
                mBinding.txtGetVerify.setVisibility(View.GONE);
                mBinding.editPass.setVisibility(View.VISIBLE);
                mBinding.editPassRepeat.setVisibility(View.GONE);
                mBinding.txtForgot.setVisibility(View.VISIBLE);
                mBinding.txtLogin.setText(getResources().getString(R.string.msg_login));
                mBinding.button.setText(getResources().getString(R.string.login));
                break;
            case 1://忘记密码
                mBinding.txtForgot.setVisibility(View.INVISIBLE);
                mBinding.editVerifyCode.setVisibility(View.VISIBLE);
                mBinding.txtGetVerify.setVisibility(View.VISIBLE);
                mBinding.editPass.setVisibility(View.VISIBLE);
                mBinding.editPassRepeat.setVisibility(View.VISIBLE);
                mBinding.txtLogin.setText(getResources().getString(R.string.login));
                mBinding.button.setText(getResources().getString(R.string.str_confirm));
                break;
            case 2://注册
                mBinding.checkbox.setVisibility(View.VISIBLE);
                mBinding.txtPrivacy.setVisibility(View.VISIBLE);
                mBinding.txtRegister.setVisibility(View.INVISIBLE);
                mBinding.editVerifyCode.setVisibility(View.VISIBLE);
                mBinding.txtGetVerify.setVisibility(View.VISIBLE);
                mBinding.editPass.setVisibility(View.VISIBLE);
                mBinding.editPassRepeat.setVisibility(View.VISIBLE);
                mBinding.txtLogin.setText(getResources().getString(R.string.login));
                mBinding.button.setText(getResources().getString(R.string.register));
                break;
        }
        clearText(mBinding.editPassRepeat, mBinding.editPass, mBinding.editPhone, mBinding.editVerifyCode);
    }

    private void clearText(EditText... editTextes){
        for(EditText editText :editTextes) {
            editText.setText("");
        }
    }




    /**
     * 提交数据
     */
    public void submit(){
        String phone = mBinding.editPhone.getText().toString();
        String pass = mBinding.editPass .getText().toString();
        String veriCode = mBinding.editVerifyCode.getText().toString();
        String invite = mBinding.editPassRepeat.getText().toString();
        if(!verifyPhone(phone)){//验证手机
            return;
        }
        switch (type){
            case -1://短信登录
                if(veriCode.length() == 0){//验证码
                    showToast(R.string.input_verify_code);
                    return;
                }
                if(!mBinding.checkbox.isChecked()){
                    showToast("请阅读并勾选《隐私条款》");
                    return;
                }
                mViewModel.loginByCode(phone,veriCode);
                break;
            case 0:
                if(!verifyPassWord(pass)){//验证密码
                    return;
                }
                mViewModel.login(phone,pass);
                break;
            case 1:
                if(!verifyPassWord(pass)){//验证密码
                    return;
                }
                if(veriCode.length() == 0){//验证码
                    showToast(R.string.input_verify_code);
                    return;
                }

                //新密码
                if(invite.length() == 0){
                    showToast(R.string.input_pass);
                    return;
                } else if(!invite.equals(pass)){
                    showToast(R.string.str_new_verify_failed);
                    return;
                }
                mViewModel.resetPwd(phone,veriCode,pass);
                break;
            case 2://注册
                if(!verifyPassWord(pass)){//验证密码
                    return;
                }
                if(veriCode.length() == 0){//验证码
                    showToast(R.string.input_verify_code);
                    return;
                }
                if(!mBinding.checkbox.isChecked()){
                    showToast("请阅读并勾选《隐私条款》");
                    return;
                }
                mViewModel.register(phone,veriCode,pass);
                break;
        }
    }

    /**
     * 验证密码
     * @param pass
     * @return
     */
    public boolean verifyPassWord(String pass){

        if(pass.length() == 0){
            showToast(R.string.input_pass);
            return false;
        } else if(!verifyPass(pass)){
            showToast(R.string.str_pass_format_failed);
            return false;
        }
        return  true;
    }


    /**
     * 验证手机号
     * @param phone
     * @return
     */
    public boolean verifyPhone(String phone ){

        if(phone.length() == 0){
            showToast(R.string.input_phone);
            return false;
        }else if(phone.length()<5 || !PhoneUtil.isMobileNO(phone)){
            showToast(R.string.str_phone_format_failed);
            return false;
        }
        return true;

    }


    /**
     * 验证密码
     * @param pass
     * @return
     */
    public boolean verifyPass(String pass){
        return AlgorithmUtils.pwdLevel(pass)>1 && pass.length()>=6 && pass.length() <=12;
    }
}
