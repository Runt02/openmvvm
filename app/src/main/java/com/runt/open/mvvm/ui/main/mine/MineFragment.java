package com.runt.open.mvvm.ui.main.mine;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.permissionx.guolindev.PermissionX;
import com.runt.open.mvvm.BuildConfig;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.fragments.BaseFragment;
import com.runt.open.mvvm.databinding.FragmentMineBinding;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.util.MyLog;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-15.
 */

public class MineFragment extends BaseFragment<FragmentMineBinding,MineViewModel> implements View.OnClickListener {

    private final  String TAG = "MineFragment";

    @Override
    public void initViews() {
    }

    @Override
    public void loadData() {
        if(UserBean.getUser() != null){
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.default_head)//图片加载出来前，显示的图片
                    .fallback(R.mipmap.default_head) //url为空的时候,显示的图片
                    .error(R.mipmap.default_head);//图片加载失败后，显示的图片
            Glide.with(getContext()).load(BuildConfig.HOST_IP_ADDR +UserBean.getUser().getHead()).apply(options).into(mBinding.img);
            mBinding.txtName.setText(UserBean.getUser().getUsername());
            mBinding.txtCoin.setText(UserBean.getUser().getCoin()+"");
            mBinding.txtSigns.setText(UserBean.getUser().getSign()+"");
            mBinding.linGroup.setVisibility(View.VISIBLE);
        }else{
            Glide.with(getContext()).load(R.mipmap.default_head).into(mBinding.img);
            mBinding.txtName.setText("未登录");
            mBinding.linGroup.setVisibility(View.GONE);
        }
        setOnClickListener(this,R.id.lin_sign,R.id.lin_coin,R.id.img,R.id.txt_name);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img:
                openAlthum();
                break;
            case R.id.txt_name://名称
                mViewModel.updateName(new HttpObserver() {
                    @Override
                    protected void onSuccess(Object data) {
                        UserBean.getUser().setUsername(data.toString());
                        mBinding.txtName.setText(data.toString());

                    }
                });
                break;
           /* case R.id.lin_coin://金币
                new BottomMenuFragment(getActivity())
                        .addMenuItems(new MenuItem("查看记录"))
                        .addMenuItems(new MenuItem("申请提现"))
                        .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView menu_item, int position) {
                                if(position == 0){
                                    startActivity(new Intent(mActivity, CoinRecordActivity.class) );
                                }else {
                                    if(mActivity.isNull(UserBean.getUser().getAlipay())){
                                        mActivity.showDialog("设置支付宝", "您还没有设置支付宝账号", "设置", "取消", new ResPonse() {
                                            @Override
                                            public void doSuccess(Object obj) {
                                                startActivity(new Intent(mActivity, CoinSettingActivity.class) );
                                            }
                                        });
                                    }else if(mActivity.isNull(UserBean.getUser().getRealname())){
                                        mActivity.showDialog("设置真实姓名", "您还没有设置真实姓名", "设置", "取消", new ResPonse() {
                                            @Override
                                            public void doSuccess(Object obj) {
                                                startActivity(new Intent(mActivity, CoinSettingActivity.class) );
                                            }
                                        });
                                    }else{
                                        startActivityForResult(new Intent(mActivity, WithDrawActivity.class),REQUEST_CODE_WITHDRAW );
                                    }
                                }
                            }
                        })
                        .show();
                break;
            case R.id.lin_sign://签到
                startActivityForResult(new Intent(getContext(), SignInActivity.class),REQUEST_CODE_SIGN);
                break;*/
        }
    }


    /**
     * 打开相册
     */
    public void openAlthum(){
        PermissionX.init(this)
                .permissions(mActivity.CAMERA_PERMISSIONS)
                .request((allGranted, grantedList, deniedList) -> {
                    if(allGranted){
                        PictureSelector
                                .create(this, PictureSelector.SELECT_REQUEST_CODE)
                                .selectPicture(true, 300, 300, 20, 20);
                    }else{
                        mActivity.showDialog("警告", "软件需要权限才能运行", "申请权限", "取消", new ResPonse() {
                            @Override
                            public void doSuccess(Object obj) {
                                openAlthum();
                            }

                            @Override
                            public void doError(Object obj) {
                            }
                        });
                    }

                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                MyLog.i("mineActivity","picturePath:"+picturePath);
                final  File file = new File(picturePath);
                mViewModel.updateHead(file).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        /*UserBean.getUser().setHead(obj.toString());
                        file.delete();
                        Glide.with(getContext()).load(BuildConfig.HOST_IP_ADDR+UserBean.getUser().getHead()) .into(mBinding.img); //获取选取的图片*/

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        file.delete();
                    }
                });
            }
        }else if(requestCode == REQUEST_CODE_SIGN && resultCode == Activity.RESULT_OK){
            loadData();
        }else if(requestCode == REQUEST_CODE_WITHDRAW && resultCode == Activity.RESULT_OK){
            loadData();
        }
    }

}
