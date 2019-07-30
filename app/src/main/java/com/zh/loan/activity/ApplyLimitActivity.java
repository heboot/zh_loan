package com.zh.loan.activity;

import android.content.DialogInterface;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityApplyLimitBinding;

public class ApplyLimitActivity extends BaseActivity<ActivityApplyLimitBinding> {

    private QMUIDialog chooseTimeDialog;

    private int type = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_limit;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("申请额度");

    }

    @Override
    public void initData() {
        initDialog();
        binding.tvDeadline.setText("半年");
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvDeadline.setOnClickListener((v)->{
            chooseTimeDialog.show();
        });
    }

    private void initDialog() {
        CharSequence[] items = {"半年", "一年", "两年"};
        chooseTimeDialog = new QMUIDialog.CheckableDialogBuilder(this)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        type = i + 1;
                        binding.tvDeadline.setText(items[i]);
                        chooseTimeDialog.dismiss();
                    }
                })
                .create();
    }
}
