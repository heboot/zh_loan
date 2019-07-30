package com.zh.loan.activity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityCashBinding;

public class CashActivity extends BaseActivity<ActivityCashBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("我要提现");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
