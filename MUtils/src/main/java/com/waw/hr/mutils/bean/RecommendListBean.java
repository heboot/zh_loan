package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.Enterprise;
import com.waw.hr.mutils.model.RecommendUser;

import java.util.List;

public class RecommendListBean extends BaseBeanEntity {

    private List<RecommendUser> list;

    public List<RecommendUser> getList() {
        return list;
    }

    public void setList(List<RecommendUser> list) {
        this.list = list;
    }
}
