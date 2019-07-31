package com.zh.loan.activity;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.bean.RateModel;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityApplyLimitBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.SignUtils;
import com.zh.loan.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApplyLimitActivity extends BaseActivity<ActivityApplyLimitBinding> {

    private QMUIDialog chooseTimeDialog;

    private int type = 0;

    private List<String> titles;

    private List<Double> rates;

    //根据利率计算应该还多少钱
    private double repaymentMoney;

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
        rateitem();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvDeadline.setOnClickListener((v)->{
            chooseTimeDialog.show();
        });
        binding.etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvRepaymentTip.setText(calculate(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.tvSubmit.setOnClickListener((v)->{
            if(StringUtils.isEmpty(binding.etMoney.getText())){
                tipDialog = DialogUtils.getFailDialog(ApplyLimitActivity.this, "请输入金额", true);
                tipDialog.show();
                return ;
            }
            applyLimit();
        });
    }

    private String calculate(String money){
        if(StringUtils.isEmpty(money)){
            return "";
        }
        repaymentMoney = Double.parseDouble(money) + (Double.parseDouble(money) * rates.get(type));
        return repaymentMoney + "(利率" + rates.get(type) + ")";
    }


    private void initDialog() {
        CharSequence[] items = new CharSequence[titles.size()];
        titles.toArray(items);
        chooseTimeDialog = new QMUIDialog.CheckableDialogBuilder(this)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        type = i ;
                        binding.tvDeadline.setText(items[i]);
                        if(!StringUtils.isEmpty(binding.etMoney.getText())){
                            binding.tvRepaymentTip.setText(calculate(binding.etMoney.getText().toString()));
                        }
                        chooseTimeDialog.dismiss();
                    }
                })
                .create();
    }

    private void applyLimit(){
        params = SignUtils.getNormalParams();
        params.put(MKey.TYPE,type);
        params.put(MKey.MONEY,binding.etMoney.getText());
        params.put(MKey.RATE,rates.get(type)+"");
        params.put(MKey.REPAYMENT,repaymentMoney);
        HttpClient.Builder.getServer().applyLimit(UserService.getInstance().getToken(),params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
            @Override
            public void onSuccess(BaseBean<Object> baseBean) {
                tipDialog = DialogUtils.getFailDialog(ApplyLimitActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }

            @Override
            public void onError(BaseBean<Object> baseBean) {
                tipDialog = DialogUtils.getFailDialog(ApplyLimitActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

    private void rateitem(){
        HttpClient.Builder.getServer().rateitem(UserService.getInstance().getToken()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<List<RateModel>>() {
            @Override
            public void onSuccess(BaseBean<List<RateModel>> baseBean) {

                titles = new ArrayList<>();
                rates = new ArrayList<>();
                for(RateModel  rateModel : baseBean.getData()){
                    titles.add(rateModel.getDuration());
                    rates.add(Double.parseDouble(rateModel.getRate()));
                }
                binding.tvDeadline.setText(titles.get(0));
                initDialog();
            }

            @Override
            public void onError(BaseBean<List<RateModel>> baseBean) {
                tipDialog = DialogUtils.getFailDialog(ApplyLimitActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }
}
