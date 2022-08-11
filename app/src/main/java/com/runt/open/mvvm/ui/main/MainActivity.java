package com.runt.open.mvvm.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.KeyEvent;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.permissionx.guolindev.PermissionX;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.base.adapter.FragmentAdapter;
import com.runt.open.mvvm.base.fragments.BaseFragment;
import com.runt.open.mvvm.data.PhoneDevice;
import com.runt.open.mvvm.databinding.ActivityMainBinding;
import com.runt.open.mvvm.listener.CustomClickListener;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.ui.SettingActivity;
import com.runt.open.mvvm.ui.loadpage.PageFragments;
import com.runt.open.mvvm.ui.login.RegisterLoginActivity;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.ui.main.mine.MineFragment;
import com.runt.open.mvvm.ui.main.service.ServiceFragment;

import java.util.Arrays;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private BaseFragment[] fragments = {new PageFragments.HomeFragment(),new ServiceFragment(),new MineFragment()} ;
    ActivityResultLauncher<Intent>  loginLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_CODE_SUCESS){
            fragments[2].loadData();//登录后重新刷新
        }else if(result.getResultCode() != RESULT_CODE_SUCESS){
            mBinding.viewPager2.setCurrentItem(0);
        }
    });

    @Override
    public void initViews() {
        ActivityResultLauncher<Intent>  settingLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_CODE_SUCESS){//已退出
                if(mBinding.viewPager2.getCurrentItem() == 2) {
                    mBinding.viewPager2.setCurrentItem(0);//设置默认第二页
                }
                fragments[2].loadData();//登录后重新刷新
            }
        });
        titleBarView.setRightClick(new CustomClickListener() {
            @Override
            protected void onSingleClick(View view) {
                settingLaunch.launch(new Intent(mContext, SettingActivity.class));//打开设置
            }
        });
        final FragmentAdapter fragmentAdapter = new FragmentAdapter(this);
        fragmentAdapter.setFragments(Arrays.asList(fragments));
        //设置当前可见Item左右可见page数，次范围内不会被销毁
        //禁用预加载
        mBinding.viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        mBinding.viewPager2.setAdapter(fragmentAdapter);
        mBinding.viewPager2.setCurrentItem(0);
        mBinding.viewPager2.setUserInputEnabled(false); //true:滑动，false：禁止滑动
        mBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                setTitleStr(position);
                mBinding.navView.getMenu().getItem(position).setChecked(true);
                if(position == 2 && UserBean.getUser() == null){
                    loginLaunch.launch(new Intent(mContext, RegisterLoginActivity.class));
                }
            }
        });
        ColorStateList csl = getResources().getColorStateList(R.color.home_nav_color);
        mBinding.navView.setItemTextColor(csl);//设置文本颜色
        mBinding.navView.setItemIconTintList(csl);//图标着色
        mBinding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);//监听

    }

    @Override
    public void loadData() {
        checkPermission();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 底部导航监听
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        for(int i = 0 ; i < mBinding.navView.getMenu().size() ; i ++){
            if(item.getItemId() == mBinding.navView.getMenu().getItem(i).getItemId()){
                mBinding.viewPager2.setCurrentItem(i);
                return true;
            }
        }
        return false;
    };

    /**
     * 设置标题
     * @param position
     */
    private void setTitleStr(int position){
        switch (position){
            case 0:
                setTitle("资讯Aljkpqla");
                break;
            case 1:
                setTitle("服务");
                break;
            case 2:
                setTitle("个人中心");
                break;
        }
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
                    PhoneDevice.setDevice(mContext);
                });
    }
}