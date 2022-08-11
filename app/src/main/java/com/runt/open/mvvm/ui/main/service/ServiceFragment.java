package com.runt.open.mvvm.ui.main.service;

import android.view.View;

import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.fragments.BaseFragment;
import com.runt.open.mvvm.base.model.ImpViewModel;
import com.runt.open.mvvm.databinding.FragmentServiceBinding;
import com.runt.open.mvvm.listener.ResPonse;

/**
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-17.
 */
public class ServiceFragment extends BaseFragment<FragmentServiceBinding, ImpViewModel> implements View.OnClickListener {

    @Override
    public void initViews() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lin_uav:
                mActivity.showDialog("联系服务商", "即将拨打服务商电话", "拨打","取消",new ResPonse() {
                    @Override
                    public void doSuccess(Object obj) {
                        mActivity.callPhone("15048325741");
                    }
                });
                break;
            case R.id.lin_bozhong:
                showDialog();
                break;
            case R.id.lin_yumi:
                showDialog();
                break;
            case R.id.lin_xiaomai:
                showDialog();
                break;
        }
    }

    public void showDialog(){
        ((BaseActivity)getActivity()).showDialog("暂无服务商", "如果您是相关服务商可致电入住", "入住","取消",new ResPonse() {
            @Override
            public void doSuccess(Object obj) {
                mActivity.showDialog("联系平台", "即将拨打平台电话", "拨打","取消",new ResPonse() {
                    @Override
                    public void doSuccess(Object obj) {
                        mActivity.callPhone("13000000000");
                    }
                });
            }
        });
    }
}
