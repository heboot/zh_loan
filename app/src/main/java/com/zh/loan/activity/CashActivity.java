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
import com.zh.loan.databinding.ActivityCashBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.SignUtils;
import com.zh.loan.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CashActivity extends BaseActivity<ActivityCashBinding> {

    private String balance;

    private String bankNumber;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("我要提现");
        loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
    }

    @Override
    public void initData() {
        balance = getIntent().getExtras().getString(MKey.BALANCE);
        bankNumber = getIntent().getExtras().getString(MKey.BANK_CARD);
        binding.tvBalance.setText(balance);
        binding.tvOption.setText("提现到" + bankNumber + "银行卡");
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvCash.setOnClickListener((v) -> {
//            if (StringUtils.isEmpty(balance) || Double.parseDouble(balance) <= 0) {
//                tipDialog = DialogUtils.getFailDialog(CashActivity.this, "没有可用余额，不能提现", true);
//                tipDialog.show();
//                return;
//            }
//            if (StringUtils.isEmpty(bankNumber) ) {
//                tipDialog = DialogUtils.getFailDialog(CashActivity.this, "没有可用银行卡，不能提现", true);
//                tipDialog.show();
//                return;
//            }
            cash();
        });
    }

    private void cash() {
        params = new HashMap<>();
        params.put(MKey.NUM, StringUtils.isEmpty(balance)?1:balance);
        loadingDialog.show();
        HttpClient.Builder.getServer().withdrawDeposit(UserService.getInstance().getToken(),params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
               dismissLoadingDialog();
                tipDialog = DialogUtils.getSuclDialog(CashActivity.this, baseBean.getMsg(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<Object> baseBean) {
                dismissLoadingDialog();
                tipDialog = DialogUtils.getFailDialog(CashActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }
}
