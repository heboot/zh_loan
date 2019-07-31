package com.zh.loan.activity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityRepaymentBinding;

public class RepaymentActivity extends BaseActivity<ActivityRepaymentBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_repayment;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("还款");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
