package com.zh.loan.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.waw.hr.mutils.bean.BillListBean;
import com.zh.loan.R;

import java.util.List;

public class BillAdapter extends BaseQuickAdapter<BillListBean.BillModel, BaseViewHolder> {

    public BillAdapter(List<BillListBean.BillModel> list) {
        super(R.layout.item_bill,list);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, BillListBean.BillModel item) {
        viewHolder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);
    }

}
