package com.runt.open.mvvm.ui.login;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.runt.open.mvvm.R;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-2-23.
 */
public class CodeTimer extends CountDownTimer {

    TextView txtGetCode;

    public CodeTimer(long millisInFuture, long countDownInterval, TextView txtGetCode) {
        super(millisInFuture, countDownInterval);
        this.txtGetCode = txtGetCode;
    }

    public void startUp(){
        txtGetCode.setEnabled(false);
        txtGetCode.setTextColor(txtGetCode.getContext().getResources().getColor(R.color.txt_enable));
        start();
    }

    @Override
    public void onTick(long l) {
        txtGetCode.setText(String.format("（%s）", l/1000));
    }

    @Override
    public void onFinish() {
        txtGetCode.setEnabled(true);
        txtGetCode.setTextColor(txtGetCode.getContext().getResources().getColor(R.color.link));
        txtGetCode.setText(txtGetCode.getContext().getResources().getString(R.string.get_verify_code));
    }

}
