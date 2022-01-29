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
        binding.txtGetVerify.setOnClickListener(onclick);
        binding.txtForgot.setOnClickListener(onclick);
        binding.txtLogin.setOnClickListener(onclick);
        binding.txtRegister.setOnClickListener(onclick);
        binding.txtPrivacy.setOnClickListener(onclick);
        long getTime = getLongProjectPrefrence(VERIFY_CODE);
        long cha = new Date().getTime() - getTime;
        if(cha <1000*60){
            CodeTimer codeTimer = new CodeTimer(cha, 1000, binding.txtGetVerify);
            codeTimer.startUp();
        }
        changeView();
        binding.editPhone.setText(getStringProjectPrefrence(Configuration.KEY_USERNAME));
        viewModel.getVerifyResult().observe(this, stringApiResult -> {
           if(stringApiResult.code == 200){

           }else{
               showToast(stringApiResult.msg);
           }
        });
        viewModel.getLoginResult().observe(this,loggedInUser -> {
            if(loggedInUser.code == 200){
                putBooleanProjectPrefrence(Configuration.IS_LOGIN,true);
                putStringProjectPrefrence(Configuration.KEY_USERNAME,binding.editPhone.getText().toString());

                UserBean user = new Gson().fromJson(new Gson().toJson(loggedInUser.data) ,UserBean.class);
                UserBean.setUser(user);
                putStringProjectPrefrence(Configuration.KEY_TOKEN, user.getToken());
                MyLog.i("registerlogin",user.toString());
                showToast(R.string.login_success);
                setResult(RESULT_CODE_SUCESS);
                finish();
            }else{
                showToast(loggedInUser.msg);
            }
        });
    }

    CustomClickListener onclick = new CustomClickListener() {
        @Override
        protected void onSingleClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    submit();
                    break;
                case R.id.txt_get_verify:

                    String phone = binding.editPhone.getText().toString();
                    if(!verifyPhone(phone)){//验证手机
                        return;
                    }
                    if(type==2){//获取注册验证码
                        viewModel.getRegisterSMS(phone);
                    }else if(type ==1){
                        viewModel.getForgetSMS( phone);
                    }else if(type == -1){
                        viewModel.getLoginSMS( phone);
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
        binding.button.setEnabled(true);
        binding.txtRegister.setVisibility(View.VISIBLE);
        binding.checkbox.setVisibility(View.GONE);
        binding.txtPrivacy.setVisibility(View.GONE);
        switch (type){
            case -1://短信登录
                binding.editVerifyCode.setVisibility(View.VISIBLE);
                binding.txtGetVerify.setVisibility(View.VISIBLE);
                binding.editPass.setVisibility(View.GONE);
                binding.editPassRepeat.setVisibility(View.GONE);
                binding.txtForgot.setVisibility(View.VISIBLE);
                binding.txtLogin.setText(getResources().getString(R.string.login));
                binding.button.setText(getResources().getString(R.string.login));
                binding.checkbox.setVisibility(View.VISIBLE);
                binding.txtPrivacy.setVisibility(View.VISIBLE);
                break;
            case 0://登录
                binding.editVerifyCode.setVisibility(View.GONE);
                binding.txtGetVerify.setVisibility(View.GONE);
                binding.editPass.setVisibility(View.VISIBLE);
                binding.editPassRepeat.setVisibility(View.GONE);
                binding.txtForgot.setVisibility(View.VISIBLE);
                binding.txtLogin.setText(getResources().getString(R.string.msg_login));
                binding.button.setText(getResources().getString(R.string.login));
                break;
            case 1://忘记密码
                binding.txtForgot.setVisibility(View.INVISIBLE);
                binding.editVerifyCode.setVisibility(View.VISIBLE);
                binding.txtGetVerify.setVisibility(View.VISIBLE);
                binding.editPass.setVisibility(View.VISIBLE);
                binding.editPassRepeat.setVisibility(View.VISIBLE);
                binding.txtLogin.setText(getResources().getString(R.string.login));
                binding.button.setText(getResources().getString(R.string.str_confirm));
                break;
            case 2://注册
                binding.checkbox.setVisibility(View.VISIBLE);
                binding.txtPrivacy.setVisibility(View.VISIBLE);
                binding.txtRegister.setVisibility(View.INVISIBLE);
                binding.editVerifyCode.setVisibility(View.VISIBLE);
                binding.txtGetVerify.setVisibility(View.VISIBLE);
                binding.editPass.setVisibility(View.VISIBLE);
                binding.editPassRepeat.setVisibility(View.VISIBLE);
                binding.txtLogin.setText(getResources().getString(R.string.login));
                binding.button.setText(getResources().getString(R.string.register));
                break;
        }
        clearText(binding.editPassRepeat,binding.editPass,binding.editPhone,binding.editVerifyCode);
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
        String phone = binding.editPhone.getText().toString();
        String pass = binding.editPass .getText().toString();
        String veriCode = binding.editVerifyCode.getText().toString();
        String invite = binding.editPassRepeat.getText().toString();
        if(!verifyPhone(phone)){//验证手机
            return;
        }
        switch (type){
            case -1://短信登录
                if(veriCode.length() == 0){//验证码
                    showToast(R.string.input_verify_code);
                    return;
                }
                if(!binding.checkbox.isChecked()){
                    showToast("请阅读并勾选《隐私条款》");
                    return;
                }
                viewModel.loginByCode(phone,veriCode);
                break;
            case 0:
                if(!verifyPassWord(pass)){//验证密码
                    return;
                }
                viewModel.login(phone,pass);
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
                viewModel.resetPwd(phone,veriCode,pass);
                break;
            case 2://注册
                if(!verifyPassWord(pass)){//验证密码
                    return;
                }
                if(veriCode.length() == 0){//验证码
                    showToast(R.string.input_verify_code);
                    return;
                }
                if(!binding.checkbox.isChecked()){
                    showToast("请阅读并勾选《隐私条款》");
                    return;
                }
                viewModel.register(phone,veriCode,pass);
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
