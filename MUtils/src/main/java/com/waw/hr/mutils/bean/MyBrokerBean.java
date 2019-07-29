package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.AdminUser;

public class MyBrokerBean extends BaseBeanEntity {

    private AdminUser broker;

    public AdminUser getBroker() {
        return broker;
    }

    public void setBroker(AdminUser broker) {
        this.broker = broker;
    }
}
