package com.zh.loan.activity;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.MainActivity;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityAboutBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.ImageUtils;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AbountActivity extends BaseActivity<ActivityAboutBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("关于我们");
    }

    @Override
    public void initData() {
        system();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
    }


    private void system(){
        HttpClient.Builder.getServer().system().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                binding.tvAppName.setText((String)baseBean.getData().get("webtitle"));
                ImageUtils.showImage(AbountActivity.this,binding.ivLogo,(String)baseBean.getData().get("logo"));
                binding.tvInfo.setText((String)baseBean.getData().get("about_us"));
            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                tipDialog = DialogUtils.getFailDialog(AbountActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }
}
