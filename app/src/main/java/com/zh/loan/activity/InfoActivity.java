package com.zh.loan.activity;

import android.content.DialogInterface;
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
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityInfoBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ImageUtils;
import com.zh.loan.utils.StringUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InfoActivity extends BaseActivity<ActivityInfoBinding> {

    private String faceUrl, reverseUrl;

    private QMUIBottomSheet chooseSheet;

    private boolean isFace;

    private final int REQUEST_ID_FACE = 40001, REQUEST_ID = 40002;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_info;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("信息完善");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvSave.setOnClickListener((v) -> {
            if (checkData()) {
                editInfo();
            }
        });
        binding.ivIdcardFace.setOnClickListener((v)->{
            isFace = true;
            if(chooseSheet == null){
                initSheel();
            }
            chooseSheet.show();
        });
        binding.ivIdcardReverse.setOnClickListener((v)->{
            if(chooseSheet == null){
                initSheel();
            }
            chooseSheet.show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (isFace) {
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                ImageUtils.showImage(this, binding.ivIdcardFace, selectList.get(0).getCompressPath());
//                UploadUtils.uploadImage(selectList.get(0).getCompressPath(), UploadUtils.getIDCardPath(), upCompletionHandler);
            } else {
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList2 = PictureSelector.obtainMultipleResult(data);
                LogUtil.e(TAG + "》", JSON.toJSONString(selectList2));
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                ImageUtils.showImage(this, binding.ivIdcardReverse, selectList2.get(0).getCompressPath());
//                UploadUtils.uploadImage(selectList2.get(0).getCompressPath(), UploadUtils.getIDCardPath(), upCompletionHandler);
            }
//
        }
    }

    private void initSheel(){
        chooseSheet = DialogUtils.getAvatarBottomSheet(this, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
            @Override
            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                if (position == 0) {
                    PictureSelector.create(InfoActivity.this)
                            .openCamera(PictureMimeType.ofImage())
                            .enableCrop(false)
                            .maxSelectNum(1).compress(true)
                            .forResult(REQUEST_ID);
                    chooseSheet.dismiss();
                } else {
                    PictureSelector.create(InfoActivity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(1).enableCrop(false)
                            .compress(true)
                            .forResult(REQUEST_ID);
                    chooseSheet.dismiss();
                }
            }
        });
    }

    private void editInfo(){

        params.put(MKey.TRUENAME,binding.etName.getText());
        params.put(MKey.PHONE1,binding.etMobile.getText());
        params.put(MKey.PHONE2,binding.etRelevanceMobile.getText());
        params.put(MKey.CITY,binding.etAddress.getText());
        params.put(MKey.BANK_CARD,binding.etBankNum.getText());
        params.put(MKey.ID_CARD,faceUrl + "," + reverseUrl);

        HttpClient.Builder.getServer().editInfo(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                tipDialog = DialogUtils.getSuclDialog(InfoActivity.this, baseBean.getMsg(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {
                tipDialog = DialogUtils.getFailDialog(InfoActivity.this, "系统错误" + baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }


    private boolean checkData() {

        if (StringUtils.isEmpty(binding.etName.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入名字", true);
            tipDialog.show();
            return false;
        }

        if (StringUtils.isEmpty(binding.etMobile.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入联系电话", true);
            tipDialog.show();
            return false;
        }

        if (StringUtils.isEmpty(binding.etRelevanceMobile.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入关联人电话", true);
            tipDialog.show();
            return false;
        }

        if (StringUtils.isEmpty(binding.etAddress.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入现居住址", true);
            tipDialog.show();
            return false;
        }

        if (StringUtils.isEmpty(binding.etBankNum.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入银行卡号", true);
            tipDialog.show();
            return false;
        }

        if (StringUtils.isEmpty(faceUrl)) {
            tipDialog = DialogUtils.getFailDialog(this, "请上传身份证正面照片", true);
            tipDialog.show();
            return false;
        }

        if (StringUtils.isEmpty(faceUrl)) {
            tipDialog = DialogUtils.getFailDialog(this, "请上传身份证反面照片", true);
            tipDialog.show();
            return false;
        }

        return true;
    }

}
