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
                    .placeholder(R.mipmap.default_head)//???????????????????????????????????????
                    .fallback(R.mipmap.default_head) //url???????????????,???????????????
                    .error(R.mipmap.default_head);//???????????????????????????????????????
            Glide.with(getContext()).load(BuildConfig.HOST_IP_ADDR +UserBean.getUser().getHead()).apply(options).into(mBinding.img);
            mBinding.txtName.setText(UserBean.getUser().getUsername());
            mBinding.txtCoin.setText(UserBean.getUser().getCoin()+"");
            mBinding.txtSigns.setText(UserBean.getUser().getSign()+"");
            mBinding.linGroup.setVisibility(View.VISIBLE);

        }else{
            Glide.with(getContext()).load(R.mipmap.default_head).into(mBinding.img);
            mBinding.txtName.setText("?????????");
            mBinding.linGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img:
                openAlthum();
                break;
            case R.id.txt_name://??????
                mViewModel.updateName(new HttpObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        UserBean.getUser().setUsername(data);
                        mBinding.txtName.setText(data);

                    }
                });
                break;
            case R.id.lin_coin://??????
                new BottomMenuFragment(getActivity())
                        .addMenuItems(new MenuItem("????????????"))
                        .addMenuItems(new MenuItem("????????????"))
                        .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView menu_item, int position) {
                                if(position == 0){
                                    startActivity(new Intent(mActivity, PageActivitys.CoinRecordActivity.class) );
                                }else {
                                    if(mActivity.isNull(UserBean.getUser().getAlipay())){
                                        mActivity.showDialog("???????????????", "?????????????????????????????????", "??????", "??????", new ResPonse() {
                                            @Override
                                            public void doSuccess(Object obj) {
                                                startActivity(new Intent(mActivity, CoinSettingActivity.class) );
                                            }
                                        });
                                    }else if(mActivity.isNull(UserBean.getUser().getRealname())){
                                        mActivity.showDialog("??????????????????", "??????????????????????????????", "??????", "??????", new ResPonse() {
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
            case R.id.lin_sign://??????
                launcher.launch(new Intent(getContext(), SignInActivity.class));
                break;
        }
    }


    /**
     * ????????????
     */
    public void openAlthum(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage()) // ??????.PictureMimeType.ofAll()?????????.ofImage()?????????.ofVideo()?????????.ofAudio()
                .imageEngine(GlideEngine.Companion.getInstance()) // ??????????????????????????????????????????
                .maxSelectNum(1) // ????????????????????????
                .minSelectNum(1) // ??????????????????
                .imageSpanCount(3) // ??????????????????
                .isReturnEmpty(true) // ????????????????????????????????????????????????
                .isAndroidQTransform(false) // ??????????????????Android Q ??????????????????????????????????????????compress(false); && .isEnableCrop(false);??????,????????????
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // ????????????Activity????????????????????????????????????
                .isSingleDirectReturn(true) // ????????????????????????????????????PictureConfig.SINGLE???????????????
                .isPreviewImage(true) // ?????????????????????
                .isCamera(true) // ????????????????????????
                .isZoomAnim(false) // ?????????????????? ???????????? ??????true
                .isEnableCrop(true) // ????????????
                .withAspectRatio(1, 1) // ???????????? ???16:9 3:2 3:4 1:1 ????????????
                .isCompress(false) // ????????????
                .cutOutQuality(100) // ?????????????????? ??????100
                .selectionMode(PictureConfig.SINGLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);//????????????onActivityResult code
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*????????????*/
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            if (data != null) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                final  File file = new File(selectList.get(0).getCutPath());
                MyLog.i("mineActivity","picturePath:"+selectList.get(0).getCutPath()+" exists:"+file.exists());
                mViewModel.updateHead(file, new HttpObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        UserBean.getUser().setHead(data);
                        Glide.with(getContext()).load(BuildConfig.HOST_IP_ADDR+UserBean.getUser().getHead()) .into(mBinding.img); //?????????????????????
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
