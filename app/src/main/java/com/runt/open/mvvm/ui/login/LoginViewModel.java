package com.runt.open.mvvm.ui.login;

import androidx.lifecycle.MutableLiveData;

import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.retrofit.api.LoginApiCenter;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.retrofit.utils.RetrofitUtils;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public class LoginViewModel extends BaseViewModel {

    MutableLiveData<LoggedInUser> loginResult = new MutableLiveData<>();

    public MutableLiveData<LoggedInUser> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        final Observable<LoggedInUser> userObservable = RetrofitUtils.getInstance().getRetrofit(LoginApiCenter.class).login(username, password);
        httpObserverOn(userObservable,new HttpObserver<LoggedInUser>(loginResult){});
    }


}
