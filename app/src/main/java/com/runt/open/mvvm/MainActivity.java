package com.runt.open.mvvm;

import android.Manifest;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.permissionx.guolindev.PermissionX;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.data.PhoneDevice;
import com.runt.open.mvvm.databinding.ActivityMainBinding;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.ui.login.RegisterLoginActivity;
import com.runt.open.mvvm.ui.main.MainViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public void initViews() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        checkPermission();
        ActivityResultLauncher<Intent>  launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_CODE_SUCESS){
                    showToast("登录成功");
                }
            }
        });
        Intent intent = new Intent(mContext, RegisterLoginActivity.class);
        launcher.launch(intent);
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