package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.ShopListModel;

import java.util.List;

public class ShopListBean extends BaseBeanEntity {

    private List<ShopListModel> list;

    public List<ShopListModel> getList() {
        return list;
    }

    public void setList(List<ShopListModel> list) {
        this.list = list;
    }
}
