package com.zh.loan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.activity.ApplyLimitActivity;
import com.zh.loan.activity.InfoActivity;
import com.zh.loan.activity.MyBalanceActivity;
import com.zh.loan.activity.ServiceActivity;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityMainBinding;
import com.zh.loan.utils.IntentUtils;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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

        binding.clytInfo.setOnClickListener((v)->{
            IntentUtils.doIntent(this,InfoActivity.class);
        });

        binding.clytBalance.setOnClickListener((v)->{
            IntentUtils.doIntent(this,MyBalanceActivity.class);
        });

        binding.clytEdu.setOnClickListener((v)->{
            IntentUtils.doIntent(this,ApplyLimitActivity.class);
        });

        binding.clytService.setOnClickListener((v)->{
            IntentUtils.doIntent(this,ServiceActivity.class);
        });
    }
}
