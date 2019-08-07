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
import com.zh.loan.BuildConfig;
import com.zh.loan.MainActivity;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityInfoBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ImageUtils;
import com.zh.loan.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
        if (UserService.getInstance().getSign() == 1) {
            binding.tvSave.setVisibility(View.GONE);
            binding.etName.setEnabled(false);
            binding.etMobile.setEnabled(false);
            binding.etRelevanceMobile.setEnabled(false);
            binding.etBankNum.setEnabled(false);
            binding.etAddress.setEnabled(false);
            binding.ivIdcardReverse.setEnabled(false);
            binding.ivIdcardFace.setEnabled(false);
        }
    }

    @Override
    public void initData() {
        if(UserService.getInstance().getSign() == 1){
            editInfo(true);
        }
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvSave.setOnClickListener((v) -> {
            if (checkData()) {
                editInfo(false);
            }
        });
        binding.ivIdcardFace.setOnClickListener((v) -> {
            isFace = true;
            if (chooseSheet == null) {
                initSheel();
            }
            chooseSheet.show();
        });
        binding.ivIdcardReverse.setOnClickListener((v) -> {
            isFace = false;
            if (chooseSheet == null) {
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
                uploadImage(true, selectList.get(0).getCompressPath());
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
                uploadImage(false, selectList2.get(0).getCompressPath());
            }
//
        }
    }


    private void uploadImage(boolean isFace, String path) {
        loadingDialog.show();
        File file = new File(path);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);

        HttpClient.Builder.getServer().upload(UserService.getInstance().getToken(), body).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<String>() {
            @Override
            public void onSuccess(BaseBean<String> baseBean) {
                tipDialog = DialogUtils.getSuclDialog(InfoActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
                if (isFace) {
                    ImageUtils.showImage(InfoActivity.this, binding.ivIdcardFace, com.example.http.BuildConfig.HTTP_SERVER + baseBean.getData());
                    faceUrl = baseBean.getData();
                } else {
                    ImageUtils.showImage(InfoActivity.this, binding.ivIdcardReverse, com.example.http.BuildConfig.HTTP_SERVER + baseBean.getData());
                    reverseUrl = baseBean.getData();
                }
                dismissLoadingDialog();
            }

            @Override
            public void onError(BaseBean<String> baseBean) {
                dismissLoadingDialog();
                tipDialog = DialogUtils.getFailDialog(InfoActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

    private void initSheel() {
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

    //     "truename": "jrjrjje",
//             "phone1": "4646664",
//             "phone2": "3166431",
//             "city": "hdjdjjd",
//             "bank_card": "*********",
//             "id_card1": "https:\/\/47.75.122.66\/upload\/20190801\/d4fe1a81f35448e9b53fc8cb07a51898.jpg",
//             "id_card2": "https:\/\/47.75.122.66\/upload\/20190801\/0bf18ca580e82e5c91d2e26664a9a071.jpg"
//}
    private void editInfo(boolean before) {
        loadingDialog.show();
        if (!before) {
            params.put(MKey.TRUENAME, binding.etName.getText());
            params.put(MKey.PHONE1, binding.etMobile.getText());
            params.put(MKey.PHONE2, binding.etRelevanceMobile.getText());
            params.put(MKey.CITY, binding.etAddress.getText());
            params.put(MKey.BANK_CARD, binding.etBankNum.getText());
            params.put(MKey.ID_CARD, faceUrl + "," + reverseUrl);
        } else {
            params.clear();
        }
        HttpClient.Builder.getServer().editInfo(UserService.getInstance().getToken(), params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (before) {
                    if (UserService.getInstance().getSign() == 1) {
                        binding.etName.setText((String) baseBean.getData().get("truename"));
                        binding.etMobile.setText((String) baseBean.getData().get("phone1"));
                        binding.etRelevanceMobile.setText((String) baseBean.getData().get("phone2"));
                        binding.etAddress.setText((String) baseBean.getData().get("city"));
                        binding.etBankNum.setText((String) baseBean.getData().get("bank_card"));
                        ImageUtils.showImage(InfoActivity.this, binding.ivIdcardReverse, (String) baseBean.getData().get("id_card1"));
                        ImageUtils.showImage(InfoActivity.this, binding.ivIdcardFace, (String) baseBean.getData().get("id_card2"));
                    }


                } else {
                    tipDialog = DialogUtils.getSuclDialog(InfoActivity.this, baseBean.getMsg(), true);
                    tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            finish();
                        }
                    });
                    tipDialog.show();
                }
            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(InfoActivity.this, baseBean.getMsg(), true);
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
