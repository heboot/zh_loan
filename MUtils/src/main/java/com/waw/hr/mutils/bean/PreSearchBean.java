package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;

import java.util.List;

public class PreSearchBean extends BaseBeanEntity {


    private List<String> hotTagList;

    private List<String> hotEnterpriseList;

    private List<String> hotCityList;

    public List<String> getHotTagList() {
        return hotTagList;
    }

    public void setHotTagList(List<String> hotTagList) {
        this.hotTagList = hotTagList;
    }

    public List<String> getHotEnterpriseList() {
        return hotEnterpriseList;
    }

    public void setHotEnterpriseList(List<String> hotEnterpriseList) {
        this.hotEnterpriseList = hotEnterpriseList;
    }

    public List<String> getHotCityList() {
        return hotCityList;
    }

    public void setHotCityList(List<String> hotCityList) {
        this.hotCityList = hotCityList;
    }
}
