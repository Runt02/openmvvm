package com.runt.open.mvvm.ui.adapter;

import android.widget.TextView;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.adapter.BaseAdapter;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.databinding.ItemCalendarDayBinding;

import java.util.List;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2019-3-26.
 */
public class CalendarAdapter extends BaseAdapter<Results.Mycalendar, ItemCalendarDayBinding> {

    public CalendarAdapter(List<Results.Mycalendar> list) {
        this.dataList = list;
    }

    @Override
    protected void onBindView(ItemCalendarDayBinding binding, int position, Results.Mycalendar mycalendar) {

        TextView txt = binding.txtDay;
        txt.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
        switch (mycalendar.getStyle()){
            case 0://已签到
                txt.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.bg_blue_circle));
                break;

            case 1://当月未签到
                txt.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.bg_enable_circle));
                break;

            case 2://今日
                txt.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.bg_red_border_circle));
                txt.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.txt_normal));
                break;

            case 3://还未到
                txt.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
                txt.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.txt_normal));
                break;
            case 4://不是本月
                txt.setBackgroundColor(binding.getRoot().getContext().getResources().getColor(R.color.white));
                txt.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.txt_enable));
                break;
            case 5://领取金币
                txt.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.bg_gold_circle));
                break;
            case 6://今日已签到
                txt.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.bg_red_circle));
                //txt.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.bg_blue_circle));
                break;

        }
        txt.setText(mycalendar.getDay());
    }
}
