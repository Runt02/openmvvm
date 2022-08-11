package com.runt.open.mvvm.ui.sign;

import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.util.DatesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2019-3-26.
 */
public class SignInViewModel extends BaseViewModel {



    public void signIn(HttpObserver<String> observer){
        httpObserverOn(commonApi.signIn(),observer);
    }

    public void getSigns(int month,HttpObserver<List<String>> observer){
        SimpleDateFormat monthdf = new SimpleDateFormat("yyyy-MM");
        Date startDate = DatesUtil.getBeginDayOfMonth(DatesUtil.getNowMonth()+month);
        httpObserverOn(commonApi.getSignsByMonth(monthdf.format(startDate)),observer);
    }

}
