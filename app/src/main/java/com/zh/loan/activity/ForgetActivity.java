package com.zh.loan.activity;

import android.content.DialogInterface;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityForgetBinding;
import com.zh.loan.databinding.ActivityRegisterBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ObserableUtils;
import com.zh.loan.utils.SignUtils;
import com.zh.loan.utils.StringUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForgetActivity extends BaseActivity<ActivityForgetBinding> {

    private String code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget;
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
    public void initListener() {
        binding.tvSendSms.setOnClickListener((v) -> {
//            binding.tvSendSms.setEnabled(false);
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

        if (StringUtils.isEmpty(binding.etConfirmPwd.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入密码", true);
            tipDialog.show();
            return;
        }

        if(!binding.etPwd.getText().equals(binding.etConfirmPwd.getText())){
            tipDialog = DialogUtils.getFailDialog(this, "两次密码不一致", true);
            tipDialog.show();
            return;
        }

        if (!code.equals(binding.etCode.getText().toString().trim())) {
            tipDialog = DialogUtils.getFailDialog(this, "验证码不正确", true);
            tipDialog.show();
            return;
        }

        params.put(MKey.PHONE, binding.etMobile.getText());

        HttpClient.Builder.getServer().pwdfind(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                tipDialog = DialogUtils.getSuclDialog(ForgetActivity.this,baseBean.getMsg(),true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
            }

            @Override
            public void onError(BaseBean<Object> baseBean) {
            }
        });


    }

    private void sendCode() {
        if (StringUtils.isEmpty(binding.etMobile.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入手机号码", true);
            tipDialog.show();
            return;
        }
        params = SignUtils.getNormalParams();
        params.put(MKey.PHONE, binding.etMobile.getText());

        HttpClient.Builder.getServer().getcode(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<String>() {
            @Override
            public void onSuccess(BaseBean<String> baseBean) {

                code = baseBean.getData();

                ObserableUtils.countdownByMILLISECONDS(60).subscribe(new Observer<Integer>() {
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
                });
            }

            @Override
            public void onError(BaseBean<String> baseBean) {
            }
        });


    }
}
