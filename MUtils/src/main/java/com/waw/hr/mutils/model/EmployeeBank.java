package com.waw.hr.mutils.model;

public class EmployeeBank {
    private int id;

    private int uid;

    private int bankId;

    private String bankNumber;

    private String picFront;

    private String picReverse;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getPicFront() {
        return picFront;
    }

    public void setPicFront(String picFront) {
        this.picFront = picFront;
    }

    public String getPicReverse() {
        return picReverse;
    }

    public void setPicReverse(String picReverse) {
        this.picReverse = picReverse;
    }
}
