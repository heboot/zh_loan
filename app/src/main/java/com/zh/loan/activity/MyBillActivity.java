package com.zh.loan.activity;

import android.bluetooth.le.AdvertiseData;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.http.HttpClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.MStatusBarUtils;
import com.waw.hr.mutils.base.BaseBean;
import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.bean.BillListBean;
import com.zh.loan.R;
import com.zh.loan.adapter.BillAdapter;
import com.zh.loan.base.BaseActivity;
import com.zh.loan.databinding.ActivityMainBinding;
import com.zh.loan.databinding.ActivityMyBillBinding;
import com.zh.loan.http.HttpObserver;
import com.zh.loan.utils.DialogUtils;

import java.util.ArrayList;
import java.util.LinkedList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyBillActivity extends BaseActivity<ActivityMyBillBinding> {

    private BillAdapter billAdapter;

    private int type = 1;


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
        billAdapter = new BillAdapter(new ArrayList<>());
        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        refundRecord();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.tvType1.setOnClickListener((v) -> {
            type = 1;
            setTypeBG();
        });
        binding.tvType2.setOnClickListener((v) -> {
            type = 2;
            setTypeBG();
        });
        binding.tvType3.setOnClickListener((v) -> {
            type = 3;
            setTypeBG();
        });
    }


    private void setTypeBG() {
        switch (type) {
            case 1:
                binding.tvType1.setBackgroundColor(0xffFA4169);
                binding.tvType1.setBackgroundColor(0xffffffff);
                binding.tvType1.setBackgroundColor(0xffffffff);
                break;
            case 2:
                binding.tvType2.setBackgroundColor(0xffFA4169);
                binding.tvType1.setBackgroundColor(0xffffffff);
                binding.tvType3.setBackgroundColor(0xffffffff);
                break;
            case 3:
                binding.tvType3.setBackgroundColor(0xffFA4169);
                binding.tvType1.setBackgroundColor(0xffffffff);
                binding.tvType2.setBackgroundColor(0xffffffff);
                break;
        }
    }

    private void refundRecord() {
        params.put(MKey.TYPE, type);
        HttpClient.Builder.getServer().refundRecord(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BillListBean>() {
            @Override
            public void onSuccess(BaseBean<BillListBean> baseBean) {
                binding.tvBalance.setText(baseBean.getData().getTotal_money()+"");
                billAdapter.getData().clear();
                billAdapter.getData().addAll(baseBean.getData().getList() == null ? new ArrayList<>() : baseBean.getData().getList());
                billAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(BaseBean<BillListBean> baseBean) {
                tipDialog = DialogUtils.getFailDialog(MyBillActivity.this, "系统错误" + baseBean.getMsg(), true);
                tipDialog.show();
            }
        });
    }

}
