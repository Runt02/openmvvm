package com.runt.open.mvvm.ui.login;

import androidx.lifecycle.MutableLiveData;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.retrofit.api.LoginApiCenter;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.retrofit.utils.RetrofitUtils;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public class LoginViewModel extends BaseViewModel {

    LoginApiCenter loginApi;

    public LoginViewModel() {
        loginApi = RetrofitUtils.getInstance().getRetrofit(LoginApiCenter.class);
    }

    MutableLiveData<UserBean> loginResult = new MutableLiveData<>();
    MutableLiveData<Integer> verifyResult = new MutableLiveData<>();
    MutableLiveData<Results.StringApiResult> resetResult = new MutableLiveData<>();
    MutableLiveData<Results.StringApiResult> registerResult = new MutableLiveData<>();
    HttpObserver<UserBean> logginObserver;

    @Override
    public void onCreate(BaseActivity activity) {
        super.onCreate(activity);
        logginObserver = new HttpObserver<UserBean>(mActivity){
            @Override
            protected void onSuccess(UserBean data) {
                loginResult.setValue(data);
            }
        };
    }

    public MutableLiveData<UserBean> getLoginResult() {
        return loginResult;
    }

    public MutableLiveData<Integer> getVerifyResult() {
        return verifyResult;
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

    /**
     * 获取验证码
     * @param url    验证码地址
     * @param phone 手机号
     */
    public void getVerifyCode(String url,String phone){
        String time = new Date().getTime()+"";
        httpObserverOnLoading(loginApi.getVerifyCode(url, phone, randomString(phone, time), time), new HttpObserver<Results.SmsResult>(){
            @Override
            protected void onSuccess(Results.SmsResult data) {
                verifyResult.setValue(0);
            }

            @Override
            protected void onFailed(HttpApiResult httpResult) {
                super.onFailed(httpResult);
                verifyResult.setValue(-1);
            }
        });
    }

    /**
     * 随机字符串
     * @param phone
     * @param time
     * @return
     */
    private String randomString(String phone,String time){
        int p =  (int) Math.round(phone.length()/6.0);
        int t = time.length()/6;
        List<String> list = new ArrayList<String>();
        for(int i = 0 ; i < 6 ; i ++){
            String str = "";
            if(i*p>phone.length()){
                str = phone.substring((i-1)*p);
            }else if((i+1)*p>phone.length()){
                str = phone.substring(i*p);
            }else{
                str = phone.substring(i*p,(i+1)*p);
            }
            String num = ((Integer.parseInt(str)*Long.parseLong(time))+"") ;
            list.add(num);
        }
        //return sb.toString();
        return plusSingle2(list);
    }

    private String plusSingle2(List<String> list){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < list.size() ; i ++){
            sb.append(list.get(i).substring(list.get(i).length()-2<0?0:list.get(i).length()-2));
        }
        return sb.toString();

    }
}
