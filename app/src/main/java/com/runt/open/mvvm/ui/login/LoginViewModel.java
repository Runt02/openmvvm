package com.runt.open.mvvm.ui.login;

import androidx.lifecycle.MutableLiveData;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.config.Configuration;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.retrofit.api.LoginApiCenter;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.retrofit.utils.RetrofitUtils;
import com.runt.open.mvvm.util.MyLog;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public class LoginViewModel extends BaseViewModel {

    LoginApiCenter loginApi;

    public LoginViewModel() {
        loginApi = RetrofitUtils.getInstance().getRetrofit(LoginApiCenter.class);
    }

    MutableLiveData<Results.StringApiResult> resetResult = new MutableLiveData<>();
    MutableLiveData<Results.StringApiResult> registerResult = new MutableLiveData<>();
    HttpObserver<UserBean> logginObserver;

    @Override
    public void onCreate(BaseActivity activity) {
        super.onCreate(activity);
        logginObserver = new HttpObserver<UserBean>(mActivity){
            @Override
            protected void onSuccess(UserBean data) {
                UserBean.setUser(data);
                mActivity.putStringProjectPrefrence(Configuration.KEY_USERNAME, data.getUsername());
                MyLog.i("registerlogin",data.toString());
                mActivity.showToast(R.string.login_success);
                mActivity.setResult(mActivity.RESULT_CODE_SUCESS);
                mActivity.finish();
            }
        };
    }


    /**
     * 密码登录
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        final Observable<HttpApiResult<UserBean>> userObservable = loginApi.login(username, password);
        httpObserverOnLoading(userObservable,logginObserver);
    }

    /**
     * 验证码登录
     * @param phone
     * @param code
     */
    public void loginByCode(String phone,String code){
        httpObserverOnLoading(loginApi.loginByCode(phone,code),logginObserver);
    }

    /**
     * 重置密码
     * @param phone
     * @param sms
     * @param pass
     */
    public void resetPwd(String phone,String sms,String pass){
        httpObserverOnLoading(loginApi.resetLoginPwd(phone, sms, pass),logginObserver);
    }

    /**
     * 注册
     * @param phone
     * @param sms
     * @param pass
     */
    public void register(String phone,String sms,String pass){
        httpObserverOnLoading(loginApi.register(phone, sms, pass), new HttpObserver<Results.StringApiResult>(){
            @Override
            protected void onSuccess(Results.StringApiResult data) {
                resetResult.setValue(data);
            }
        });
    }

    /**
     * 注册密码
     * @param phone
     */
    public void getRegisterSMS(String phone){
        getVerifyCode("getRegisterSMS",phone);
    }

    /**
     * 忘记密码
     * @param phone
     */
    public void getForgetSMS(String phone){
        getVerifyCode("getForgetSMS",phone);
    }

    /**
     * 登录验证码
     * @param phone
     */
    public void getLoginSMS(String phone){
        getVerifyCode("getLoginSMS",phone);
    }

}
