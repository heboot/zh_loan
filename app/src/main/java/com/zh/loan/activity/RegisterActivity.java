package com.zh.loan.activity;

import android.content.DialogInterface;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityRegisterBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ObserableUtils;
import com.zh.loan.utils.StringUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private String code;

    private Observer countDownObserver;

    private Observable observable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
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
    }

    @Override
    public void initListener() {
        binding.tvSendSms.setOnClickListener((v) -> {
            binding.tvSendSms.setEnabled(false);
            sendCode();
        });
        binding.tvLogin.setOnClickListener((v) -> {
            register();
        });
    }


    private void register() {
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
        if (!StringUtils.isEmpty(code) &&  !code.equals(binding.etCode.getText().toString().trim())) {
            tipDialog = DialogUtils.getFailDialog(this, "验证码不正确", true);
            tipDialog.show();
            return;
        }

        params.put(MKey.PHONE, binding.etMobile.getText());
        params.put(MKey.CODE, binding.etCode.getText());
        params.put(MKey.PASSWORD, binding.etPwd.getText());
        HttpClient.Builder.getServer().register(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<String>() {
            @Override
            public void onSuccess(BaseBean<String> baseBean) {
                UserService.getInstance().setToken(baseBean.getData());
                tipDialog = DialogUtils.getSuclDialog(RegisterActivity.this,baseBean.getMsg(),true);
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
                tipDialog = DialogUtils.getFailDialog(RegisterActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });


    }
}
