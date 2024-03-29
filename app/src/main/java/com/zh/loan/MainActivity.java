package com.zh.loan;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.http.HttpClient;
import com.example.http.UploadHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.waw.hr.mutils.LogUtil;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.activity.ApplyLimitActivity;
import com.zh.loan.activity.InfoActivity;
import com.zh.loan.activity.MyBalanceActivity;
import com.zh.loan.activity.MyBillActivity;
import com.zh.loan.activity.ServiceActivity;
import com.zh.loan.activity.SettingActivity;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityMainBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ImageUtils;
import com.zh.loan.utils.IntentUtils;
import com.zh.loan.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private QMUIBottomSheet chooseAvatarSheet;

    private final int REQUEST_CAMERA = 40001, REQUEST_PHOTO = 40002;

    private QMUIDialog qmuiDialog;

    private double status;

    private String tipMsg;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserService.getInstance().isLoginValue()) {
            myindex();
        }
    }

    @Override
    public void initListener() {
        binding.prContainer.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                myindex();
            }
        });

        binding.clytInfo.setOnClickListener((v) -> {
            if (UserService.getInstance().isLogin()) {
                IntentUtils.doIntent(this, InfoActivity.class);
            }
        });

        binding.clytBalance.setOnClickListener((v) -> {
            if (UserService.getInstance().isLogin()) {
//                IntentUtils.doIntent(this, MyBalanceActivity.class);
                if (status == 2) {
                    tipDialog = DialogUtils.getFailDialog(MainActivity.this, StringUtils.isEmpty(tipMsg) ? "暂时没有可处理的账单" : tipMsg, true);
                    tipDialog.show();
                    return;
                }
                IntentUtils.doIntent(this, MyBillActivity.class);
            }
        });

        binding.clytEdu.setOnClickListener((v) -> {
            if (UserService.getInstance().isLogin()) {
                if (UserService.getInstance().getStatus() == 2) {
                    IntentUtils.intent2StatusTipActivity(MainActivity.this, "审核结果", "审核处理中", "已提交申请，等待审核处理", R.mipmap.icon_wating);
                    return;
                } else if (UserService.getInstance().getStatus() == 3) {
                    if (qmuiDialog == null) {
                        qmuiDialog = new QMUIDialog.MessageDialogBuilder(this)
                                .setMessage("当前账户状态不能再次申请额度，请检查是否有额度未使用和账单未还清").
                                        addAction("确定", new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                qmuiDialog.dismiss();
                                            }
                                        })
                                .create();
                    }
                    qmuiDialog.show();
                    return;
                }
                IntentUtils.doIntent(this, ApplyLimitActivity.class);
            }
        });

        binding.clytService.setOnClickListener((v) -> {
            IntentUtils.doIntent(this, ServiceActivity.class);
        });

        binding.ivAvatar.setOnClickListener((v) -> {
            if (UserService.getInstance().isLogin()) {
                if (chooseAvatarSheet == null) {
                    chooseAvatarSheet = DialogUtils.getAvatarBottomSheet(MainActivity.this, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            if (position == 0) {
                                PictureSelector.create(MainActivity.this)
                                        .openCamera(PictureMimeType.ofImage())
                                        .enableCrop(true)
                                        .withAspectRatio(1, 1)
                                        .maxSelectNum(1).compress(true)
                                        .forResult(REQUEST_CAMERA);
                                chooseAvatarSheet.dismiss();
                            } else {
                                PictureSelector.create(MainActivity.this)
                                        .openGallery(PictureMimeType.ofImage())
                                        .maxSelectNum(1).enableCrop(true)
                                        .compress(true).withAspectRatio(1, 1)
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

    private void balance() {
        HttpClient.Builder.getServer().myAccount(UserService.getInstance().getToken()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                status = (double) baseBean.getData().get("status");
                tipMsg = (String) baseBean.getData().get("msg");
            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MainActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
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
            avatarPath = selectList.get(0).getCompressPath();

            uploadAvatar();
//                UploadUtils.uploadImage(selectList.get(0).getCompressPath(), UploadUtils.getIDCardPath(), upCompletionHandler);
        }
    }

    private String avatarPath;

    private void uploadAvatar() {

        loadingDialog.show();

        File file = new File(avatarPath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);

        HttpClient.Builder.getServer().updateImg(UserService.getInstance().getToken(), body).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<String>() {
            @Override
            public void onSuccess(BaseBean<String> baseBean) {
                dismissLoadingDialog();
                tipDialog = DialogUtils.getSuclDialog(MainActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
                ImageUtils.showImage(MainActivity.this, binding.ivAvatar, baseBean.getData());
            }

            @Override
            public void onError(BaseBean<String> baseBean) {
                dismissLoadingDialog();
                tipDialog = DialogUtils.getFailDialog(MainActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }


    private void myindex() {
        balance();
        HttpClient.Builder.getServer().myindex(UserService.getInstance().getToken()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                binding.prContainer.finishRefresh();
                UserService.getInstance().setPhone((String) baseBean.getData().get("phone"));
                UserService.getInstance().setHeadImg((String) baseBean.getData().get("headimg"));
                UserService.getInstance().setSign((Double) baseBean.getData().get("sign"));
                UserService.getInstance().setStatus((Double) baseBean.getData().get("status"));
                binding.tvMobile.setText(UserService.getInstance().getPhone());
                ImageUtils.showAvatar(MainActivity.this, binding.ivAvatar, UserService.getInstance().getHeadImg());
            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                binding.prContainer.finishRefresh();
                tipDialog = DialogUtils.getFailDialog(MainActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

}
