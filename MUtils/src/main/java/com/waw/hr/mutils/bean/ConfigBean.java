package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;
import com.waw.hr.mutils.model.BankModel;

import java.util.List;

public class ConfigBean extends BaseBeanEntity {

    //客服电话
    private String kfTel;

    private List<BankModel> bankList;

    private String version;

    private String recommendInfo;

    private String recommendIcon;

    public String getKfTel() {
        return kfTel;
    }

    public void setKfTel(String kfTel) {
        this.kfTel = kfTel;
    }

    public List<BankModel> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankModel> bankList) {
        this.bankList = bankList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(String recommendInfo) {
        this.recommendInfo = recommendInfo;
    }

    public String getRecommendIcon() {
        return recommendIcon;
    }

    public void setRecommendIcon(String recommendIcon) {
        this.recommendIcon = recommendIcon;
    }
}
