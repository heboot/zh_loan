package com.waw.hr.mutils.model;

import java.util.List;

public class ShopListModel {
    private int id;

    private String title;

    private List<ShopEntity> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ShopEntity> getList() {
        return list;
    }

    public void setList(List<ShopEntity> list) {
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
