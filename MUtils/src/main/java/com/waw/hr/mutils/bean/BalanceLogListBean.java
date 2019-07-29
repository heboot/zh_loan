package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.BalanceModel;

import java.util.List;

public class BalanceLogListBean extends BaseBeanEntity {

    private List<BalanceModel> list;

    public List<BalanceModel> getList() {
        return list;
    }

    public void setList(List<BalanceModel> list) {
        this.list = list;
    }
}
