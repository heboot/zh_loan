package com.zh.loan.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.alibaba.fastjson.JSON;
import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.waw.hr.mutils.LogUtil;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.ContactsModel;
import com.zh.loan.BuildConfig;
import com.zh.loan.MAPP;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityRegisterBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.ContactsUtils;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ObserableUtils;
import com.zh.loan.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> implements EasyPermissions.PermissionCallbacks {

    private String code;

    private Observer countDownObserver;

    private Observable observable;

    private final int RC_CONTACTS = 999;

    private QMUIDialog permissionDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
        permissionDialog = new QMUIDialog.MessageDialogBuilder(this)
                .setMessage("系统需要获取通讯录权限")
                .setTitle("提醒")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        permissionDialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            String[] perms = {Manifest.permission.READ_CONTACTS};
                            EasyPermissions.requestPermissions(RegisterActivity.this, "需要获取通讯录权限",
                                    RC_CONTACTS, perms);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", MAPP.mapp.getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            RegisterActivity.this.startActivity(intent);
                        }
                    }
                })
                .addAction("退出", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        permissionDialog.dismiss();
                        finish();
                    }
                })
                .create();
    }

    @Override
    public void initData() {

        countDownObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                if (integer <= 0) {
                    binding.tvSendSms.setEnabled(true);
                    binding.tvSendSms.setText("获取验证码");
                } else {
                    binding.tvSendSms.setText(integer + "");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) {
                permissionDialog.show();
            }
        } else {
            boolean result = false;
            try {
                result = ContactsUtils.checkReadContacts(this);
            } catch (Exception e) {
                result = false;
            }
            if (!result) {
                permissionDialog.show();
            }
        }


    }

    @Override
    public void initListener() {
        binding.tvSendSms.setOnClickListener((v) -> {

            sendCode();
        });
        binding.tvLogin.setOnClickListener((v) -> {
            register();
        });
        binding.vClearPhone.setOnClickListener((v) -> {
            binding.etMobile.setText("");
        });
        binding.vClearPwd.setOnClickListener((v) -> {
            binding.etPwd.setText("");
        });
    }


    private void register() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) {
                permissionDialog.show();
                return;
            }
        } else {
            boolean result = false;
            try {
                result = ContactsUtils.checkReadContacts(this);
            } catch (Exception e) {
                result = false;
            }
            if (!result) {
                permissionDialog.show();
                return;
            }
        }

        if (StringUtils.isEmpty(binding.etMobile.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入手机号码", true);
            tipDialog.show();
            return;
        }
        if (StringUtils.isEmpty(binding.etCode.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入验证码", true);
            tipDialog.show();
            return;
        }
        if (StringUtils.isEmpty(binding.etPwd.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入密码", true);
            tipDialog.show();
            return;
        }
        if (!StringUtils.isEmpty(code) && !code.equals(binding.etCode.getText().toString().trim())) {
            tipDialog = DialogUtils.getFailDialog(this, "验证码不正确", true);
            tipDialog.show();
            return;
        }

        loadingDialog.show();

        params.put(MKey.PHONE, binding.etMobile.getText());
        params.put(MKey.CODE, StringUtils.isEmpty(binding.etCode.getText()) ? "" : binding.etCode.getText());
        params.put(MKey.PASSWORD, binding.etPwd.getText());
        List<ContactsModel> list = ContactsUtils.getAllContacts(this);
        if (list != null && list.size() > 0) {
            params.put(MKey.ADDRESS_LIST, JSON.toJSONString(list));
        }else{
            params.put(MKey.ADDRESS_LIST, "");
        }
        HttpClient.Builder.getServer().register(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<String>() {
            @Override
            public void onSuccess(BaseBean<String> baseBean) {
                dismissLoadingDialog();
                UserService.getInstance().setToken(baseBean.getData());
                tipDialog = DialogUtils.getSuclDialog(RegisterActivity.this, baseBean.getMsg(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<String> baseBean) {
                dismissLoadingDialog();
                tipDialog = DialogUtils.getFailDialog(RegisterActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });


    }

    private void sendCode() {
        if (StringUtils.isEmpty(binding.etMobile.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入手机号码", true);
            tipDialog.show();
            return;
        }
        binding.tvSendSms.setEnabled(false);
        params.put(MKey.PHONE, binding.etMobile.getText());

        HttpClient.Builder.getServer().getcode(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<String>() {
            @Override
            public void onSuccess(BaseBean<String> baseBean) {

                code = baseBean.getData();

                observable = ObserableUtils.countdown(60);

                observable.subscribe(countDownObserver);
            }

            @Override
            public void onError(BaseBean<String> baseBean) {
                binding.tvSendSms.setEnabled(true);
                tipDialog = DialogUtils.getFailDialog(RegisterActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogUtil.e(TAG + "2>", "onPermissionsGranted" + requestCode + ">>>>>" + JSON.toJSONString(perms));
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        LogUtil.e(TAG + "3>", "denied");
        permissionDialog.show();
    }
}
