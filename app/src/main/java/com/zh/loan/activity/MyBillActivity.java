package com.zh.loan.activity;

import android.content.DialogInterface;
import android.view.View;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.bean.BillListBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.common.TipType;
import com.zh.loan.databinding.ActivityMyBillBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.IntentUtils;
import com.zh.loan.utils.StringUtils;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyBillActivity extends BaseActivity<ActivityMyBillBinding> {

    private double refund_status, delay_status;


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        refundRecord();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvRepayment.setOnClickListener((v) -> {
//            全部还款状态标志：1为待还款 2为审核中 3为已还款4为还款失败（1,3保持原样）
//            延期还款状态标志：1为审核中2为申请成功3为申请失败（2保持原样）
            if (refund_status == 2) {
                IntentUtils.intent2StatusTipActivity(this, "审核结果", "审核处理中", "已提交申请，等待审核处理", R.mipmap.icon_wating);
                return;

            } else if (refund_status == 4) {
                IntentUtils.intent2StatusTipActivity(this, "还款结果", "还款失败", "", R.mipmap.icon_fail, TipType.REPAYMENT_FAIL);
                return;
            }

            allRefund();
        });
        binding.tvRepaymentOutdate.setOnClickListener((v) -> {
            if (delay_status == 1) {
                IntentUtils.intent2StatusTipActivity(this, "审核结果", "审核处理中", "已提交申请，等待审核处理", R.mipmap.icon_wating);
                return;
            } else if (delay_status == 3) {
                IntentUtils.intent2StatusTipActivity(this, "申请失败", "申请失败", "", R.mipmap.icon_fail, TipType.APPLY_FAIL);
                return;
            }
            IntentUtils.doIntent(this, DelayRefundActivity.class);
        });
    }

    private void allRefund() {
        params.clear();
        HttpClient.Builder.getServer().allRefund(UserService.getInstance().getToken(), params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                IntentUtils.intent2StatusTipActivity(MyBillActivity.this, "审核结果", "审核处理中", baseBean.getMsg(), R.mipmap.icon_wating);
                finish();
            }

            @Override
            public void onError(BaseBean<Object> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MyBillActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

    private void refundRecord() {
        HttpClient.Builder.getServer().refundRecord(UserService.getInstance().getToken(), params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                binding.tvBalance.setText(StringUtils.isEmpty((String) baseBean.getData().get("total_money")) ? "0" : (String) baseBean.getData().get("total_money"));

                refund_status = (double) baseBean.getData().get("refund_status");
                delay_status = (double) baseBean.getData().get("delay_status");

                if ((double) baseBean.getData().get("status") == 2) {
                    binding.tvOutdate.setVisibility(View.VISIBLE);
                } else {
                    binding.tvOutdate.setVisibility(View.GONE);
                }

                binding.tvEnddatev.setText((String)baseBean.getData().get("refund_time"));

            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MyBillActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

}
