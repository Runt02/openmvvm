package com.runt.open.mvvm.ui.main.mine;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.runt.open.mvvm.BuildConfig;
import com.runt.open.mvvm.R;
import com.runt.open.mvvm.base.fragments.BaseFragment;
import com.runt.open.mvvm.config.Configuration;
import com.runt.open.mvvm.databinding.FragmentMineBinding;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.ui.coin.WithDrawActivity;
import com.runt.open.mvvm.ui.coin.CoinSettingActivity;
import com.runt.open.mvvm.ui.loadpage.PageActivitys;
import com.runt.open.mvvm.ui.login.UserBean;
import com.runt.open.mvvm.ui.sign.SignInActivity;
import com.runt.open.mvvm.util.GlideEngine;
import com.runt.open.mvvm.util.MyLog;
import sakura.bottommenulibrary.bottompopfragmentmenu.BottomMenuFragment;
import sakura.bottommenulibrary.bottompopfragmentmenu.MenuItem;

import java.io.File;
import java.util.List;

/**
 * My father is Object, ites purpose of
 *
 * @purpose Created by Runt (qingingrunt2010@qq.com) on 2020-9-15.
 */

public class MineFragment extends BaseFragment<FragmentMineBinding,MineViewModel> implements View.OnClickListener {

    private final  String TAG = "MineFragment";
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        refreshUi();
    });
    @Override
    public void initViews() {
        mViewModel.getUserBean();
        UserBean.onUpdate.observe(this, userBean -> {
            mActivity.putStringProjectPrefrence(Configuration.KEY_USERINFO, new Gson().toJson(userBean));
            refreshUi();
        });
    }

    @Override
    public void loadData() {
        setOnClickListener(this,R.id.lin_sign,R.id.lin_coin,R.id.img,R.id.txt_name);
        refreshUi();
    }

    public void refreshUi(){
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img:
                openAlthum();
                break;
            case R.id.txt_name://名称
                mViewModel.updateName(new HttpObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        UserBean.getUser().setUsername(data);
                        mBinding.txtName.setText(data);

                    }
                });
                break;
            case R.id.lin_coin://金币
                new BottomMenuFragment(getActivity())
                        .addMenuItems(new MenuItem("查看记录"))
                        .addMenuItems(new MenuItem("申请提现"))
                        .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView menu_item, int position) {
                                if(position == 0){
                                    startActivity(new Intent(mActivity, PageActivitys.CoinRecordActivity.class) );
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
                                        launcher.launch(new Intent(mActivity, WithDrawActivity.class) );
                                    }
                                }
                            }
                        })
                        .show();
                break;
            case R.id.lin_sign://签到
                launcher.launch(new Intent(getContext(), SignInActivity.class));
                break;
        }
    }


    /**
     * 打开相册
     */
    public void openAlthum(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.Companion.getInstance()) // 外部传入图片加载引擎，必传项
                .maxSelectNum(1) // 最大图片选择数量
                .minSelectNum(1) // 最小选择数量
                .imageSpanCount(3) // 每行显示个数
                .isReturnEmpty(true) // 未选择数据时点击按钮是否可以返回
                .isAndroidQTransform(false) // 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // 设置相册Activity方向，不设置默认使用系统
                .isSingleDirectReturn(true) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true) // 是否可预览图片
                .isCamera(true) // 是否显示拍照按钮
                .isZoomAnim(false) // 图片列表点击 缩放效果 默认true
                .isEnableCrop(true) // 是否裁剪
                .withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isCompress(false) // 是否压缩
                .cutOutQuality(100) // 裁剪输出质量 默认100
                .selectionMode(PictureConfig.SINGLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            if (data != null) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                final  File file = new File(selectList.get(0).getCutPath());
                MyLog.i("mineActivity","picturePath:"+selectList.get(0).getCutPath()+" exists:"+file.exists());
                mViewModel.updateHead(file, new HttpObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        UserBean.getUser().setHead(data);
                        Glide.with(getContext()).load(BuildConfig.HOST_IP_ADDR+UserBean.getUser().getHead()) .into(mBinding.img); //获取选取的图片
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
