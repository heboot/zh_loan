package com.zh.loan.activity;

import android.content.DialogInterface;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.bean.BillListBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityMyBillBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.IntentUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyBillActivity extends BaseActivity<ActivityMyBillBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_bill;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("我的账单");
    }

    @Override
    public void initData() {
        refundRecord();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvRepayment.setOnClickListener((v) -> {
            allRefund();
        });
        binding.tvRepaymentOutdate.setOnClickListener((v) -> {
            IntentUtils.doIntent(this,RepaymentActivity.class);
        });
    }

    private void allRefund(  ) {
        HttpClient.Builder.getServer().allRefund(UserService.getInstance().getToken(),params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                tipDialog = DialogUtils.getSuclDialog(MyBillActivity.this,   baseBean.getMsg(), true);
                tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        IntentUtils.intent2StatusTipActivity(MyBillActivity.this,"审核结果","审核处理中",baseBean.getMsg(),R.mipmap.icon_wating);
                        finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<Object> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MyBillActivity.this,  baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

    private void refundRecord(  ) {
        HttpClient.Builder.getServer().refundRecord(UserService.getInstance().getToken(),params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BillListBean>() {
            @Override
            public void onSuccess(BaseBean<BillListBean> baseBean) {
                binding.tvBalance.setText(baseBean.getData().getTotal_money()+"");
            }

            @Override
            public void onError(BaseBean<BillListBean> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MyBillActivity.this,  baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

}
