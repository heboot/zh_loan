package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.Enterprise;

import java.util.List;

public class EnterpriseListBean extends BaseBeanEntity {

    private List<Enterprise> list;

    public List<Enterprise> getList() {
        return list;
    }

    public void setList(List<Enterprise> list) {
        this.list = list;
    }
}
