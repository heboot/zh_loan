package com.zh.loan.activity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityStatusTipBinding;

public class StatusTipActivity extends BaseActivity<ActivityStatusTipBinding> {

    private String title;

    private String tip;

    private String tip2;

    private int bg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_status_tip;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);

    }

    @Override
    public void initData() {
        title = getIntent().getExtras().getString(MKey.TITLE);
        tip = getIntent().getExtras().getString(MKey.TIP);
        tip2 = getIntent().getExtras().getString(MKey.TIP2);
        bg = getIntent().getExtras().getInt(MKey.TYPE);
        binding.includeToolbar.tvTitle.setText(title);
        binding.tvTip1.setText(tip);
        binding.tvTip2.setText(tip2);
        binding.ivIcon.setBackgroundResource(bg);
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
    }
}
