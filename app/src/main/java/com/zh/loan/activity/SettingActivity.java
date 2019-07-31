package com.zh.loan.activity;

import android.view.View;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.zh.loan.R;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityServiceBinding;
import com.zh.loan.databinding.ActivitySettingBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.service.UserService;
import com.zh.loan.utils.DialogUtils;
import com.zh.loan.utils.IntentUtils;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    private QMUIBottomSheet logoutSheet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initUI() {
        MStatusBarUtils.setActivityLightMode(this);
        QMUIStatusBarHelper.translucent(this);
        binding.includeToolbar.tvTitle.setText("设置");
        if(!UserService.getInstance().isLoginValue()){
            binding.clytLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        logoutSheet = DialogUtils.getLogoutBottomSheet(this, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
            @Override
            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                if(position == 2){
                    logoutSheet.dismiss();
                }else{
                    UserService.getInstance().logout();
                    IntentUtils.doIntent(SettingActivity.this,LoginActivity.class);
                    finish();
                }
            }
        });
        system();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.clytLogout.setOnClickListener((v) -> {
            logoutSheet.show();
        });
        binding.clytAbout.setOnClickListener((v)->{
            IntentUtils.doIntent(this,AbountActivity.class);
        });
    }

    private void system(){
        HttpClient.Builder.getServer().system().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<Map>() {
            @Override
            public void onSuccess(BaseBean<Map> baseBean) {
                binding.tvVersion.setText((String)baseBean.getData().get("card"));
//                binding.tv.setText((String)baseBean.getData().get("webtitle"));
//                ImageUtils.showImage(AbountActivity.this,binding.ivLogo,(String)baseBean.getData().get("logo"));
//                binding.tvInfo.setText((String)baseBean.getData().get("about_us"));
            }

            @Override
            public void onError(BaseBean<Map> baseBean) {
                tipDialog = DialogUtils.getFailDialog(SettingActivity.this, baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }
}
