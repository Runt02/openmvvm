package com.runt.open.mvvm.ui.sign;

import android.util.Log;
import androidx.recyclerview.widget.GridLayoutManager;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.databinding.ActivitySignsBinding;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.ui.adapter.CalendarAdapter;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.util.DatesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2019-3-26.
 */
public class SignInActivity extends BaseActivity<ActivitySignsBinding,SignInViewModel> {

    List<Results.Mycalendar> list = new ArrayList<>();
    CalendarAdapter adapter = new CalendarAdapter(list);
    SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat monthdf = new SimpleDateFormat("yyyy/MM");
    SimpleDateFormat secondsdf = new SimpleDateFormat("dd");
    int last = 0 ;

    HttpObserver<List<String>> observer = new HttpObserver<List<String>>() {
        @Override
        protected void onSuccess(List<String> data) {
            last = (int) mBinding.txtCalendarTitle.getTag();
            String today = datesdf.format(new Date());
            if(data.contains(today)){
                mBinding.btnSign.setText("已签到");
                mBinding.btnSign.setEnabled(false);
            }
            initCalendar(data);
            loadMothBtn(getStartDate());
        }

        @Override
        protected void onFailed(HttpApiResult error) {
            super.onFailed(error);
            loadMothBtn(getStartDate());
        }
    };
    @Override
    public void initViews() {
        mBinding.lastMonth.setOnClickListener(v->{
            v.setEnabled(false);
            mBinding.txtCalendarTitle.setTag(last - 1);
            mViewModel.getSigns(last-1,observer);
        });
        mBinding.nextMonth.setOnClickListener(v->{
            v.setEnabled(false);
            mBinding.txtCalendarTitle.setTag(last+ 1);
            mViewModel.getSigns(last+1,observer);;});
        mBinding.btnSign.setOnClickListener(v->{mViewModel.signIn(new HttpObserver<String>(this) {
            @Override
            protected void onSuccess(String data) {
                showToast("赠送金币"+data+"个");
                UserBean.getUser().setCoin(UserBean.getUser().getCoin()+Integer.parseInt(data.toString()));
                UserBean.getUser().setSign(UserBean.getUser().getSign()+1);
                mBinding.txtSigns.setText(UserBean.getUser().getSign()+"天");
                mBinding.btnSign.setEnabled(false);
                mBinding.btnSign.setText("已签到");
                setResult(RESULT_OK);
                mViewModel.getSigns(last,observer);
            }

            @Override
            protected void onFailed(HttpApiResult error) {
                super.onFailed(error);
                if(error.code == 610){
                    mBinding.btnSign.setEnabled(false);
                    mBinding.btnSign.setText("已签到");
                }
            }
        });});
        mBinding.recycler.setLayoutManager(new GridLayoutManager(mContext,7));
        mBinding.recycler.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        initCalendar(new ArrayList<>());
        mBinding.txtCalendarTitle.setTag(last);
        mViewModel.getSigns(last,observer);
    }

    /**
     * 加载🗓
     * @param signList
     */
    private  void initCalendar(List<String> signList ){
        list.clear();
        Date startDate = getStartDate();
        Date endDate = DatesUtil.getEndDayOfMonth(DatesUtil.getNowMonth()+last);
        int days = (int) ((endDate.getTime() - startDate.getTime())/(60000*60*24));
        int week = DatesUtil.getWeekOfDate(startDate);
        long starttime = startDate.getTime()-((60000*60*24*week-1));
        int lastWeek = DatesUtil.getWeekOfDate(endDate);
        long endtime = endDate.getTime()+((60000*60*24*(6-lastWeek)));
        days+=week+1;
        days+=(6-lastWeek);
        int newDays = (int) ((endtime- starttime)/(60000*60*24));
        //Log.e(TAG,"days:"+days +" newDays:"+newDays+" month:"+(DatesUtil.getNowMonth()+last)+" endDate:"+datesdf.format(endDate));
        for(int i  = 0 ; i < days ; i ++ ){
            Results.Mycalendar calendar = new Results.Mycalendar();
            long dateTime = starttime+(i*(60000l*60*24));
            calendar.setDateTime(dateTime);
            calendar.setDay(secondsdf.format(new Date(dateTime)));
            /*if(coinList.contains(datesdf.format(new Date(dateTime)))){//是否赠送了金币
                calendar.setStyle(5);
            }else */if(dateTime < startDate.getTime() || dateTime > endDate.getTime()){//不是本月的日期
                calendar.setStyle(4);
            }else if(datesdf.format(new Date()).equals(datesdf.format(new Date(dateTime)))&& !signList.contains(datesdf.format(new Date(dateTime)))){//今天
                Log.e(TAG,"今天"+datesdf.format(new Date()));
                calendar.setStyle(2);
            }else if(datesdf.format(new Date()).equals(datesdf.format(new Date(dateTime)))&& signList.contains(datesdf.format(new Date(dateTime)))){//今天
                calendar.setStyle(6);
            }else if(new Date().getTime()<dateTime){//还没到的日期
                calendar.setStyle(3);
            }else if(signList.contains(datesdf.format(new Date(dateTime)))){//是否签到
                calendar.setStyle(0);
            }else{
                calendar.setStyle(1);
            }
            //Log.e(TAG,"day:"+datesdf.format(new Date(dateTime)) + " sign:"+signs.contains(datesdf.format(new Date(dateTime))));
            list.add(calendar);
        }
        adapter.notifyDataSetChanged();
        mBinding.txtCalendarTitle.setText(monthdf.format(startDate));
        loadMothBtn(startDate);

    }

    private Date getStartDate(){
        secondsdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        datesdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return DatesUtil.getBeginDayOfMonth(DatesUtil.getNowMonth()+last);
    }

    private void loadMothBtn(Date startDate){

        try {
            if(monthdf.format(startDate).equals(monthdf.format(new Date()))|| new Date().getTime()<startDate.getTime()) {//超过指定日期禁止点击
                mBinding.nextMonth.setEnabled(false);
            }else{
                mBinding.nextMonth.setEnabled(true);
            }
            if(monthdf.format(startDate).equals("2021/09") || monthdf.parse("2021/09").getTime()>startDate.getTime()){//超过指定日期禁止点击
                mBinding.lastMonth.setEnabled(false);
            }else{
                mBinding.lastMonth.setEnabled(true);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
