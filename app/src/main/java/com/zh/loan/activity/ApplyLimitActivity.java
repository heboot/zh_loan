package com.zh.loan.activity;

import android.content.DialogInterface;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.bean.RateModel;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityApplyLimitBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApplyLimitActivity extends BaseActivity<ActivityApplyLimitBinding> {

    private QMUIDialog chooseTimeDialog;

    private int type = 1;

    private List<String> titles;

    private List<Double> rates;

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
    }

    private void initDialog() {
        CharSequence[] items = new CharSequence[titles.size()];
        titles.toArray(items);
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
