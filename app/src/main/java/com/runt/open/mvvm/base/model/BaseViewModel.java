package com.runt.open.mvvm.base.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModel;
import com.runt.open.mvvm.BuildConfig;
import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.data.HttpApiResult;
import com.runt.open.mvvm.data.Results;
import com.runt.open.mvvm.listener.ResPonse;
import com.runt.open.mvvm.retrofit.AndroidScheduler;
import com.runt.open.mvvm.retrofit.api.CommonApiCenter;
import com.runt.open.mvvm.retrofit.observable.HttpObserver;
import com.runt.open.mvvm.retrofit.utils.RetrofitUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2021/11/11 0011.
 */
public class BaseViewModel extends ViewModel {

    protected BaseActivity mActivity;
    protected CommonApiCenter commonApi = RetrofitUtils.getInstance().getCommonApi();

    public void onCreate(BaseActivity activity) {
        this.mActivity = activity;
    }

    public void checkUpdate(boolean showTip){
        Observable<HttpApiResult<Results.ApkVersion>> appUpdate = commonApi.getAppUpdate();
        HttpObserver<Results.ApkVersion> observer = new HttpObserver<Results.ApkVersion>(mActivity) {
            @Override
            protected void onSuccess(Results.ApkVersion version) {
                if (version.code > BuildConfig.VERSION_CODE) {
                    mActivity.showDialog("有新版本", version.detail, "升级", "取消", new ResPonse() {
                        @Override
                        public void doSuccess(Object obj) {
                            String downloadUrl = version.access.indexOf("http")> -1? version.access:BuildConfig.HOST_IP_ADDR + version.access;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                boolean hasInstallPermission = mActivity.getPackageManager().canRequestPackageInstalls();
                                if (!hasInstallPermission) {
                                    mActivity.showDialog("当前没有安装权限", "当前没有安装权限", "去设置", "稍后安装", new ResPonse() {
                                        @Override
                                        public void doSuccess(Object obj) {
                                            //跳转至“安装未知应用”权限界面，引导用户开启权限
                                            Uri selfPackageUri = Uri.parse("package:" + mActivity.getPackageName());
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);
                                            mActivity.startActivity(intent);
                                        }
                                    });
                                    return;
                                }
                            }
                            downloadApk(downloadUrl);
                        }

                    });
                }else if(showTip){
                    mActivity.showToast("当前是最新版本");
                }
            }

        };
        if(showTip){
            httpObserverOnLoading(appUpdate, observer);
        }else{
            httpObserverOn(appUpdate,observer);
        }
    }


    private void downloadApk(String url){
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mActivity.showToast("下载失败"+e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int code = response.code();
                Log.d("downloadFile", "code:"+code);
                if(code != 200){
                    mActivity.showToast("下载失败"+code);
                    return;
                }
                FileOutputStream fos = null;
                InputStream input = null;
                File file = null;
                byte[] buf = new byte[2048];
                int len = 0;
                try {
                    Log.d("downloadFile", "下载文件:"+url);
                    input = response.body().byteStream();
                    long total = response.body().contentLength();
                    Log.d("downloadFile", "total:"+total);
                    file = new File(mActivity.getExternalFilesDir(null).getAbsolutePath() + "/");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    file = new File(file.getPath() + "/" + url.substring(url.lastIndexOf("/")));
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = input.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        float progress = (sum * 1.0f / total * 100);
                        // 下载中
                        Log.d("downloadFile", "下载进度:" + progress);
                    }
                    Log.d("downloadFile", file.getPath());
                    // 下载完成
                    File finalFile = file;
                    mActivity.runOnUiThread(() -> {
                        mActivity.showDialog("安装", "新版安装包下载完成", "安装", "取消", new ResPonse() {
                            @Override
                            public void doSuccess(Object obj) {
                                openAPK(finalFile);
                            }
                        });
                    });
                } catch (Exception e) {
                    Log.d("downloadFileException", e.toString());
                    Log.d("downloadFileException", "Url:${call.request().url}");
                    mActivity.showToast("下载失败"+e);
                    file.delete();
                    call.cancel();
                } finally {
                    fos.flush();
                    input.close();
                    fos.close();
                }

            }

        });
    }
    /**
     * 打开apk文件
     *
     * @param file
     */
    private void openAPK(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri photoURI = null;
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            photoURI = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".fileprovider", file);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            photoURI = Uri.fromFile(file);
        }
        intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
        mActivity.startActivity(intent);
    }

    /**
     * 网络请求观察
     * @param observable
     * @param <T>
     * @return
     */
    public <T> void httpObserverOn(Observable<T> observable, HttpObserver observer){
        observable.subscribeOn(Schedulers.io())//指定网络请求在io后台线程中进行
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(observer);
    }


    /**
     * 网络请求观察
     * @param observable
     * @param <T>
     * @return
     */
    public <T> void httpObserverOnLoading(Observable<T> observable, HttpObserver observer){
        observable.subscribeOn(Schedulers.io())//指定网络请求在io后台线程中进行
                .doOnSubscribe(disposable -> {
                    mActivity.showLoadingDialog("");
                })
                .observeOn(AndroidScheduler.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
                .doOnComplete(() -> {
                    mActivity.dissmissLoadingDialog();
                })
                .subscribe(observer);
    }



}
