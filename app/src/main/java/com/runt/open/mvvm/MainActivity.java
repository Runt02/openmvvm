package com.runt.open.mvvm;

import android.Manifest;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.permissionx.guolindev.PermissionX;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.data.PhoneDevice;
import com.runt.open.mvvm.databinding.ActivityMainBinding;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.ui.main.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public void initViews() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        checkPermission();
    }

    private void showPermissionDialog(){

        showDialog("警告", "软件需要权限才能运行", "申请权限", "退出", new ResPonse() {
            @Override
            public void doSuccess(Object obj) {
                checkPermission();
            }

            @Override
            public void doError(Object obj) {
                finish();
                System.exit(0);
            }
        });
    }
    private void checkPermission(){
        PermissionX.init(MainActivity.this)
                .permissions(Manifest.permission.READ_PHONE_STATE)
                .request((allGranted, grantedList, deniedList) -> {
                    if(allGranted){
                        PhoneDevice.setDevice(mContext);
                    }else{
                        showPermissionDialog();
                    }

                });
    }
}