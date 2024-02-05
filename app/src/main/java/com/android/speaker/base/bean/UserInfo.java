package com.android.speaker.base.bean;

import android.content.SharedPreferences;

import com.android.speaker.MainApplication;
import com.android.speaker.base.Constants;
import com.google.gson.Gson;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private final static String KEY_USER_MODEL = "key_user_model";

    private static UserInfo sUserInfo;

    private String phone;
    private String token;
    private String userId;
    private String name;
    private String avatar;
    // 性别 0男 1女 2 未知
    private int gender = 0;
    // 是否实名认证 0否 1是
    private int authentication;
    // 身份证
    private String idCard;
    // 真实姓名
    private String realName;
    private String birthday;
    private boolean isVip;
    private String level;
    // 是否第三方登录
    private boolean isThirdLogin;
    // 微信是否绑定
    private boolean wx;

    public synchronized static UserInfo getInstance() {
        if (sUserInfo == null) {
            SharedPreferences shareInfo = MainApplication.getInstance().getSharedPreferences(Constants.SP_USERINFO, 0);
            String json = shareInfo.getString(KEY_USER_MODEL, "");
            sUserInfo = new Gson().fromJson(json, UserInfo.class);
            if (sUserInfo == null) {
                sUserInfo = new UserInfo();
            }
        }
        return sUserInfo;
    }

    private UserInfo() {

    }

    public void storeUserInfo() {
        SharedPreferences shareInfo = MainApplication.getInstance().getSharedPreferences(Constants.SP_USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(KEY_USER_MODEL, new Gson().toJson(this));
        editor.apply();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String userPhone) {
        this.phone = userPhone;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String url) {
        this.avatar = url;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isThirdLogin() {
        return isThirdLogin;
    }

    public void setThirdLogin(boolean thirdLogin) {
        isThirdLogin = thirdLogin;
    }

    public boolean isWx() {
        return wx;
    }

    public void setWx(boolean wx) {
        this.wx = wx;
    }

    public void cleanUserInfo() {
        token = "";
        userId = "";
        name = "";
        avatar = "";
        gender = 0;
        authentication = 0;
        idCard = "";
        realName = "";
        birthday = "";
        isVip = false;
        level = "";
        isThirdLogin = false;
        wx = false;
        storeUserInfo();
    }

}
