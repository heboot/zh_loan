package com.zh.loan.activity;

import android.content.DialogInterface;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.MValue;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.bean.BillListBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityDelayRefundBinding;
import com.zh.loan.databinding.ActivityMyBillBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.IntentUtils;
import com.zh.loan.utils.SignUtils;
import com.zh.loan.utils.StringUtils;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DelayRefundActivity extends BaseActivity<ActivityDelayRefundBinding> {

    private double money; private String date;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_delay_refund;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("延期还款");
    }

    @Override
    public void initData() {
        delayRefund(true);
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvRepayment.setOnClickListener((v) -> {
            delayRefund(false);
        });
    }

    private void delayRefund(boolean isFirst) {
        if(isFirst){
            params.clear();
        }else{
            params = SignUtils.getNormalParams();
            params.put(MKey.MONEY,money);
            params.put(MKey.DAY,date);
        }
        HttpClient.Builder.getServer().delayRefund(UserService.getInstance().getToken(),params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                if(baseBean.getData() instanceof  Map){
                    money = (double) ((Map)baseBean.getData()).get("money");
                    date = (String)  ((Map)baseBean.getData()).get("day");
                    binding.tvBalance.setText( money+"");
                    binding.tvDate.setText(date);
                    if(!isFirst){
                        tipDialog = DialogUtils.getSuclDialog(DelayRefundActivity.this,   baseBean.getMsg(), true);
                        tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                finish();
                            }
                        });
                        tipDialog.show();
                    }
                }

            }

            @Override
            public void onError(BaseBean<Object> baseBean) {
                tipDialog = DialogUtils.getFailDialog(DelayRefundActivity.this,  baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }


}
