package com.zh.loan.activity;

import android.content.DialogInterface;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityBalanceBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.IntentUtils;
import com.zh.loan.utils.SignUtils;
import com.zh.loan.utils.StringUtils;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyBalanceActivity extends BaseActivity<ActivityBalanceBinding> {

    private String bank_card;

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
        balance();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.clyt1.setOnClickListener((v)->{
            if(StringUtils.isEmpty(binding.tvBalance.getText())){
                tipDialog = DialogUtils.getFailDialog(MyBalanceActivity.this,"暂无可提现余额", true);
                tipDialog.show();
                return;
            }
            IntentUtils.intent2CashActivity(this,binding.tvBalance.getText().toString(),bank_card);
        });
        binding.clyt2.setOnClickListener((v)->{
            IntentUtils.doIntent(this,MyBillActivity.class);
        });
    }

    private void balance(){
        HttpClient.Builder.getServer().myAccount(UserService.getInstance().getToken()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {

                binding.tvBalance.setText(StringUtils.isEmpty((String)baseBean.getData().get("money"))?"0.0":(String)baseBean.getData().get("money"));
                bank_card = (String)baseBean.getData().get("bank_card");

            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MyBalanceActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

}
