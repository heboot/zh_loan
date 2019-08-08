package com.zh.loan.activity;

import android.content.DialogInterface;
import android.view.View;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.common.TipType;
import com.zh.loan.databinding.ActivityStatusTipBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ImageUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StatusTipActivity extends BaseActivity<ActivityStatusTipBinding> {

    private String title;

    private String tip;

    private String tip2;

    private int bg;

    private TipType tipType;

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
        tipType = (TipType)getIntent().getExtras().get(MKey.TIP_TYPE);

        binding.includeToolbar.tvTitle.setText(title);
        binding.tvTip1.setText(tip);
        binding.tvTip2.setText(tip2);
        binding.ivIcon.setBackgroundResource(bg);

        if(tipType == TipType.REPAYMENT_FAIL || tipType == TipType.APPLY_FAIL){
            binding.tvSave.setVisibility(View.VISIBLE);
        }
    }


//    前端加两个按钮，一个是还款失败页面，一个是申请失败页面，在页面下方加一个确认按钮，分别对应两个接口，还款失败确认（subRefund1）、申请失败确认（subApply），都是get请求，没有返回值
    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvSave.setOnClickListener((v)->{
            if(tipType == TipType.APPLY_FAIL){
                HttpClient.Builder.getServer().subApply(UserService.getInstance().getToken()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
                    @Override
                    public void onSuccess(BaseBean<Object> baseBean) {
                        dismissLoadingDialog();
                        tipDialog = DialogUtils.getSuclDialog(StatusTipActivity.this, baseBean.getMsg(), true);
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
                        tipDialog = DialogUtils.getFailDialog(StatusTipActivity.this, baseBean.getMsg(), true);
                        tipDialog.show();
                    }
                });
            }else if(tipType == TipType.REPAYMENT_FAIL){
                HttpClient.Builder.getServer().subRefund1(UserService.getInstance().getToken()).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Object>() {
                    @Override
                    public void onSuccess(BaseBean<Object> baseBean) {
                        dismissLoadingDialog();
                        tipDialog = DialogUtils.getSuclDialog(StatusTipActivity.this, baseBean.getMsg(), true);
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
                        tipDialog = DialogUtils.getFailDialog(StatusTipActivity.this, baseBean.getMsg(), true);
                        tipDialog.show();
                    }
                });
            }
        });
    }
}
