package com.zh.loan;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.http.HttpClient;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.waw.hr.mutils.LogUtil;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.activity.ApplyLimitActivity;
import com.zh.loan.activity.InfoActivity;
import com.zh.loan.activity.MyBalanceActivity;
import com.zh.loan.activity.ServiceActivity;
import com.zh.loan.activity.SettingActivity;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityMainBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ImageUtils;
import com.zh.loan.utils.IntentUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private QMUIBottomSheet chooseAvatarSheet;

    private final int REQUEST_CAMERA = 40001, REQUEST_PHOTO = 40002;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserService.getInstance().isLoginValue()){
            myindex();
        }
    }

    @Override
    public void initListener() {

        binding.clytInfo.setOnClickListener((v) -> {
            if (UserService.getInstance().isLogin()) {
                IntentUtils.doIntent(this, InfoActivity.class);
            }
        });

        binding.clytBalance.setOnClickListener((v) -> {
            if (UserService.getInstance().isLogin()) {
                IntentUtils.doIntent(this, MyBalanceActivity.class);
            }
        });

        binding.clytEdu.setOnClickListener((v) -> {
            if (UserService.getInstance().isLogin()) {
                IntentUtils.doIntent(this, ApplyLimitActivity.class);
            }
        });

        binding.clytService.setOnClickListener((v) -> {
            IntentUtils.doIntent(this, ServiceActivity.class);
        });

        binding.ivAvatar.setOnClickListener((v)->{
            if (UserService.getInstance().isLogin()) {
                if(chooseAvatarSheet == null){
                    chooseAvatarSheet  = DialogUtils.getAvatarBottomSheet(MainActivity.this, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            if (position == 0) {
                                PictureSelector.create(MainActivity.this)
                                        .openCamera(PictureMimeType.ofImage())
                                        .enableCrop(true)
                                        .maxSelectNum(1).compress(true)
                                        .forResult(REQUEST_CAMERA);
                                chooseAvatarSheet.dismiss();
                            } else {
                                PictureSelector.create(MainActivity.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .maxSelectNum(1).enableCrop(true)
                                        .compress(true)
                                        .forResult(REQUEST_PHOTO);
                                chooseAvatarSheet.dismiss();
                            }
                        }
                    });
                }
                chooseAvatarSheet.show();
            }
        });

        binding.vSetting.setOnClickListener((v) -> {
                IntentUtils.doIntent(this, SettingActivity.class);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                ImageUtils.showImage(this, binding.ivAvatar, selectList.get(0).getCompressPath());
//                UploadUtils.uploadImage(selectList.get(0).getCompressPath(), UploadUtils.getIDCardPath(), upCompletionHandler);
        }
    }



    private void myindex(){
        HttpClient.Builder.getServer().myindex(UserService.getInstance().getToken()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                UserService.getInstance().setPhone((String) baseBean.getData().get("phone"));
                UserService.getInstance().setHeadImg((String) baseBean.getData().get("headimg"));
                UserService.getInstance().setSign((Double) baseBean.getData().get("sign"));
                UserService.getInstance().setStatus((Double) baseBean.getData().get("status"));
                binding.tvMobile.setText(UserService.getInstance().getPhone());
                ImageUtils.showAvatar(MainActivity.this,binding.ivAvatar,UserService.getInstance().getHeadImg());
            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MainActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

}
