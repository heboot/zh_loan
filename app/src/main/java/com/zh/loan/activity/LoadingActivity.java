package com.zh.loan.activity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.zh.loan.MainActivity;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.IntentUtils;
import com.zh.loan.utils.ObserableUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoadingActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        ObserableUtils.countdown(3).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                if(integer == 0){
                    if(UserService.getInstance().isLoginValue()){
                        IntentUtils.doIntent(LoadingActivity.this, MainActivity.class);
                        finish();
                    }else{
                        IntentUtils.doIntent(LoadingActivity.this, LoginActivity.class);
                        finish();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
