package com.runt.open.mvvm.base.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.MyApplication;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.model.BaseViewModel;
import com.runt.open.mvvm.base.model.ViewModelFactory;
import com.runt.open.mvvm.listener.ResPonse;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import dmax.dialog.SpotsDialog;

/**
 * activity 封装
 * Created by Administrator on 2021/10/27 0027.
 */
public abstract class BaseActivity<B extends ViewBinding,VM extends BaseViewModel> extends AppCompatActivity {

    protected  B binding;
    protected  VM viewModel;
    protected String TAG ;
    public final String[] FILE_PERMISSIONS = new String []{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public final String[] LOCATION_PERMISSIONS = new String []{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    public final String[] CAMERA_PERMISSIONS = new String[]{ FILE_PERMISSIONS[0],FILE_PERMISSIONS[1], Manifest.permission.CAMERA};
    public final String[] CAMERA_RECORD_PERMISSIONS = new String[]{ FILE_PERMISSIONS[0],FILE_PERMISSIONS[1], Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

    public static final int RESULT_LOGIN = 100,RESULT_LOGIN_RECREATE = 103,RESULT_BIND = 101,RESULT_SENDEDFILES = 105,RESULT_DISSCONNECT = 104,
            RESULT_UPDATEUSER =  115,RESULT_LOGOUT = 113, REQUEST_CODE_ACTIVITY = 333;
    public static final int REQUEST_CODE_LOGOUT = 20009,/*退出*/
            REQUEST_CODE_ORDER = 10010,/*订单详情*/
            REQUEST_CODE_LOGIN = 20010,/*登录*/
            REQUEST_CODE_SCAN = 20011/*扫描请求*/,
            REQUEST_CODE_PIC = 20012,/*选择图片*/
            REQUEST_CODE_PAYPASS = 20112,/*支付验证*/
            REQUEST_CODE_PAYPASS_FOR_ALIPAY = 20110,/*支付宝修改支付验证*/
            REQUEST_CODE_PAYPASS_FOR_REALNAME = 20111,/*支付宝修改支付验证*/
            REQUEST_CODE_PERMISSION = 20013,/*请求权限*/
            REQUEST_VERSION_PERMISSION = 20013,/*检查更新*/
            REQUEST_INSTALL_PERMISSION = 20014,/*请求安装权限*/
            REQUEST_CODE_MANAGE_GROUP = 20015,/*选择分组*/
            REQUEST_CODE_MANAGE_DEL = 20016,/*选择删除*/
            REQUEST_CODE_MANAGE_EDIT = 20017,/*选择编辑*/
            REQUEST_CODE_MANAGE_ADD = 20018,/*选择添加*/
            REQUEST_CODE_MANAGE_DRAFT = 20019,/*选择草稿编辑*/
            REQUEST_CODE_WEPAY = 20020,/*微信支付*/
            REQUEST_CODE_ALIPAY = 20021,/*支付宝*/
            REQUEST_CODE_PINKAGE = 20022,/*包邮设置*/
            REQUEST_INSTALL_APK = 20200,/*请求安装权限*/
            REQUEST_CODE_COMMENT_REFRESH = 20222,/*请求安装权限*/
            RESULT_CODE_DRAFT = 4041/*草稿*/,
            RESULT_CODE_UPDATED = 4042/*更新*/,
            RESULT_CODE_FAILD = 4044/*失败*/,
            RESULT_CODE_SUCESS = 4046/*成功*/,
            RESULT_CODE_CANCEL = 4043/*取消*/;
    protected Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get genericity "B"
        setStatusBarBgColor(R.color.white);
        setStatusBarTextColor(true);
        final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        try {
            Class<B> entityClass = (Class<B>) type.getActualTypeArguments()[0];
            Method method = entityClass.getMethod("inflate", LayoutInflater.class);//get method from name "inflate";
            binding = (B) method.invoke(entityClass,getLayoutInflater());//execute method to create a objct of viewbind;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Class<VM> vmClass = (Class<VM>) type.getActualTypeArguments()[1];
        viewModel = new ViewModelProvider(this,getViewModelFactory()).get(vmClass);
        setContentView(binding.getRoot());
        mContext = this;
        try {
            //设置坚屏 一定要放到try catch里面，否则会崩溃
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception e) {
        }
        TAG = this.getClass().getSimpleName();
        initViews();
    }

    public abstract void initViews();


    public boolean isNull(Object object){
        return object == null || object.toString().trim().equals("") || object.equals("null");
    }


    AlertDialog dialog;
    /**
     * 显示弹框
     * @param title
     * @param msg
     * @param btnOk
     * @param btnCancel
     * @param resPonse
     */
    public void showDialog(String title, String msg, String btnOk,String btnCancel,final ResPonse resPonse){
        showDialog(title,msg,null,btnOk,btnCancel,resPonse,false);
    }

    private void showDialog(String title, String msg, String hint,String btnOk,String btnCancel,final  ResPonse resPonse,boolean isEdit){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.TransparentDialog);
        builder.setCancelable(false);
        final View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog,null);
        TextView titleView = view.findViewById(R.id.txt_title);
        TextView cancelView = view.findViewById(R.id.txt_cancel);
        final TextView textView = view.findViewById(R.id.msg);
        if(isEdit){
            textView.setEnabled(true);
        }else{
            textView.setBackground(null);
        }
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(resPonse !=null){
                    resPonse.doError(null);
                }
            }
        });
        cancelView.setText(btnCancel);
        TextView confirmView = view.findViewById(R.id.txt_confirm);
        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(resPonse !=null){
                    resPonse.doSuccess(textView.getText().toString());
                }
            }
        });
        confirmView.setText(btnOk);
        confirmView.requestFocus();
        if(hint != null){
            textView.setHint(hint);
        }
        if(msg != null){
            textView.setText(msg);
        }
        titleView.setText(title);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    AlertDialog loadingDialog;
    /**
     * 显示加载弹框
     * @param msg
     */
    public void showLoadingDialog(String msg){
        if(loadingDialog != null  && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        loadingDialog = new SpotsDialog.Builder().setContext(mContext).build();
        loadingDialog.setMessage(msg);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    /**
     * 清除弹框
     */
    public void dissmissLoadingDialog(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    public ViewModelProvider.Factory getViewModelFactory(){
        return ViewModelFactory.getInstance();
    }

    public void setStatusBarTransparent(boolean isBlack){
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setStatusBarTextColor(isBlack);
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param colorId
     */
    public void setStatusBarBgColor(@ColorRes int colorId) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(colorId));
    }

    /**
     * 修改状态栏文本颜色
     * @param isBlack
     */
    public void setStatusBarTextColor(boolean isBlack){
        View decor = getWindow().getDecorView();
        if (isBlack) {
            decor.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 隐藏虚拟按键
     */
    public void hideBottomUIMenu() {
        //隐藏虚拟按键
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY  ;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, ev)) { //判断用户点击的是否是输入框以外的区域
                hideSoftKeyboard ();   //收起键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }
    /**
     * 判断软键盘输入法是否弹出
     */
    public boolean isKeyboardVisbility(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            return true;//键盘显示中
        } else {
            return false;//键盘未显示
        }
    }
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }


    long mExitTime= 0 ;
    /**
     * 返回键退出程序
     */
    public void backExit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            //此方法导致app关闭后重启
            MyApplication.getApplication().quitApp();
            System.exit(0);
            //quitApp();
        }
    }
    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int msg){
        showToast(getString(msg));
    }

    /**
     * 获取文件保存路径 sdcard根目录/download/文件名称
     * @param fileUrl
     * @return
     */
    public static String getSaveFilePath(String fileUrl){
        String fileName=fileUrl.substring(fileUrl.lastIndexOf("/")+1,fileUrl.length());//获取文件名称
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return storePath + File.separator +fileName;
    }


    protected boolean onBackKeyDown() {
        onBackPressed();
        return false;
    }

}
