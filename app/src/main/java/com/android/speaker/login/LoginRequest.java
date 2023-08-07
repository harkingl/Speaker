package com.android.speaker.login;

import android.content.Context;
import android.text.TextUtils;

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest extends BaseRequest<UserInfo> {
    private String code;
    private String mobile;
    private String grant_type;
    private String loginToken;
    private String exID;
    private String openId;
    public LoginRequest(Context context, String code, String mobile, String loginToken, String exID, String openId, String grant_type) {
        super(context);
        this.code = code;
        this.mobile = mobile;
        this.loginToken = loginToken;
        this.exID = exID;
        this.openId = openId;
        this.grant_type = grant_type;
    }

    @Override
    protected String url() {
        return UrlManager.USER_LOGIN;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        if(!TextUtils.isEmpty(code)) {
            obj.put("code", code);
        }
        if(!TextUtils.isEmpty(mobile)) {
            obj.put("mobile", mobile);
        }
        if(!TextUtils.isEmpty(loginToken)) {
            obj.put("loginToken", loginToken);
        }
        if(!TextUtils.isEmpty(exID)) {
            obj.put("exID", exID);
        }
        if(!TextUtils.isEmpty(openId)) {
            obj.put("openId", openId);
        }

        obj.put("grant_type", grant_type);
        return obj.toString();
    }

    @Override
    protected UserInfo result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        UserInfo info = UserInfo.getInstance();
        if(data != null) {
            info.setPhone(mobile);
            info.setUserId(data.optString("memberId"));
            info.setToken(data.optString("access_token"));
//            info.setName(data.optString("username"));
            info.storeUserInfo();
        }
        return info;
    }
}
