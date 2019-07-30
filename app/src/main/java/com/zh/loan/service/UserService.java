package com.zh.loan.service;

import com.waw.hr.mutils.MKey;
import com.waw.hr.mutils.PreferencesUtils;
import com.zh.loan.MAPP;
import com.zh.loan.activity.LoginActivity;
import com.zh.loan.utils.IntentUtils;
import com.zh.loan.utils.StringUtils;

public class UserService {

    private UserService() {
    }

    private String token;

    private static UserService userService;

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public boolean isLogin(){
        if(StringUtils.isEmpty(token)){
            IntentUtils.doIntent(MAPP.mapp.getCurrentActivity(),LoginActivity.class);
            return false;
        }
        return !StringUtils.isEmpty(token);
    }

    public String getToken() {
        if(StringUtils.isEmpty(token)){
            token = PreferencesUtils.getString(MAPP.mapp,MKey.TOKEN);
        }
        return token;
    }

    public void setToken(String token) {
        PreferencesUtils.putString(MAPP.mapp,MKey.TOKEN,token);
        this.token = token;
    }
}
