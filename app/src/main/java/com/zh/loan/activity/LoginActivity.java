package com.zh.loan.activity;

import android.content.DialogInterface;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityLoginBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.IntentUtils;
import com.zh.loan.utils.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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

        binding.tvLogin.setOnClickListener((v)->{
            login();
        });

        binding.llytRegister.setOnClickListener((v) -> {
            IntentUtils.doIntent(this,RegisterActivity.class);
        });

        binding.tvForget.setOnClickListener((v) -> {
            IntentUtils.doIntent(this,ForgetActivity.class);
        });

    }

    private void login(){

        if (StringUtils.isEmpty(binding.etMobile.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入手机号码", true);
            tipDialog.show();
            return;
        }
        if (StringUtils.isEmpty(binding.etPwd.getText())) {
            tipDialog = DialogUtils.getFailDialog(this, "请输入密码", true);
            tipDialog.show();
            return;
        }

        params.put(MKey.PHONE, binding.etMobile.getText());
        params.put(MKey.PASSWORD, binding.etPwd.getText());
        HttpClient.Builder.getServer().login(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<String>() {
            @Override
            public void onSuccess(BaseBean<String> baseBean) {
                UserService.getInstance().setToken(baseBean.getData());
                finish();
            }

            @Override
            public void onError(BaseBean<String> baseBean) {
                tipDialog = DialogUtils.getFailDialog(LoginActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

}
