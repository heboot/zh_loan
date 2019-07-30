package com.zh.loan.activity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityBalanceBinding;

public class MyBalanceActivity extends BaseActivity<ActivityBalanceBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_balance;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("我的余额");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
    }
}
