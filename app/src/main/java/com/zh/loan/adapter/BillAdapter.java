package com.zh.loan.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.waw.hr.mutils.bean.BillListBean;
import com.zh.loan.R;
import com.zh.loan.common.CommonValue;

import java.util.List;

public class BillAdapter extends BaseQuickAdapter<BillListBean.BillModel, BaseViewHolder> {

    public BillAdapter(List<BillListBean.BillModel> list) {
        super(R.layout.item_bill,list);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, BillListBean.BillModel item) {

        viewHolder.setText(R.id.tv_title, "第"  + item.getNumber() + "期    " + item.getMoney())
                .setText(R.id.tv_time, item.getRefund_time());

        if(item.getStatus()  == CommonValue.BillStatus.WAIT){
            viewHolder.setText(R.id.tv_status,"待还款");
        }else if(item.getStatus()  == CommonValue.BillStatus.REVIEW){
            viewHolder.setText(R.id.tv_status,"正在审核");
            ((TextView)viewHolder.getView(R.id.tv_status)).setTextColor(0xffFA4169);
        }else if(item.getStatus()  == CommonValue.BillStatus.REFUND){
            viewHolder.setText(R.id.tv_status,"还款成功");
        }else if(item.getStatus()  == CommonValue.BillStatus.TIMEOUT){
            viewHolder.setText(R.id.tv_status,"已逾期");
        }
    }

}
