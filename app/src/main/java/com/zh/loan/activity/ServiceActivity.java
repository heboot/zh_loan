package com.zh.loan.activity;

import android.content.Intent;
import android.net.Uri;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityServiceBinding;

public class ServiceActivity extends BaseActivity<ActivityServiceBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("客服");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.clytQq.setOnClickListener((v)->{
            final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin="+535+"&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        });
        binding.clytMobile.setOnClickListener((v)->{
            Intent dialIntent =  new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + "186"));//跳转到拨号界面，同时传递电话号码
            startActivity(dialIntent);
        });
    }
}
