package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.EmployeeBank;

public class BankInfoBean extends BaseBeanEntity {

    private EmployeeBank bankInfo;

    public EmployeeBank getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(EmployeeBank bankInfo) {
        this.bankInfo = bankInfo;
    }
}
