package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;

public class FollowBean extends BaseBeanEntity {
    private int is_follow;
    private int is_join;

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getIs_join() {
        return is_join;
    }

    public void setIs_join(int is_join) {
        this.is_join = is_join;
    }
}
