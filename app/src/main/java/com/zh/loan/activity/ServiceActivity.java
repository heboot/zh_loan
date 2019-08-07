package com.zh.loan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIDeviceHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityServiceBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.utils.DialogUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ServiceActivity extends BaseActivity<ActivityServiceBinding> {

    private String qq, phone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("客服");
    }

    @Override
    public void initData() {
        system();
    }

    private void system() {
        HttpClient.Builder.getServer().system().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                binding.tvPhone.setText((String) baseBean.getData().get("phone"));
                binding.tvQq.setText((String) baseBean.getData().get("qq"));
                qq = (String) baseBean.getData().get("qq");
                phone = (String) baseBean.getData().get("phone");
//                binding.tv.setText((String)baseBean.getData().get("webtitle"));
//                ImageUtils.showImage(AbountActivity.this,binding.ivLogo,(String)baseBean.getData().get("logo"));
//                binding.tvInfo.setText((String)baseBean.getData().get("about_us"));
            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                tipDialog = DialogUtils.getFailDialog(ServiceActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.clytQq.setOnClickListener((v) -> {
            if (!isQQClientAvailable(this)) {
                tipDialog = DialogUtils.getFailDialog(this, "手机上没有安装QQ", true);
                tipDialog.show();
                return;
            }
            final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        });
        binding.clytMobile.setOnClickListener((v) -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));//跳转到拨号界面，同时传递电话号码
            startActivity(dialIntent);
        });
    }

    public boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
