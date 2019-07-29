package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.Enterprise;

public class EnterpriseDetailBean extends BaseBeanEntity {

    private Enterprise enterprise;

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
}
