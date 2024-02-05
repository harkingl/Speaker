package com.android.speaker.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.server.okhttp.RequestListener;

public class LoginTask implements Runnable {
    private static final String TAG = "LoginTask";
    private Context context;
    private String code;
    private String mobile;
    private String loginToken;
    // 一键登录用
    private String exID;
    private String grant_type;
    // 微信登录用
    private String openId;
    private RequestListener<UserInfo> requestListener;
    public LoginTask(Context context, String code, String mobile, String grant_type, RequestListener<UserInfo> requestListener) {
        this.context = context;
        this.code = code;
        this.mobile = mobile;
        this.grant_type = grant_type;
        this.requestListener = requestListener;
    }

    public LoginTask(Context context, String loginToken, String exID, String openId, String grant_type, RequestListener<UserInfo> requestListener) {
        this.context = context;
        this.loginToken = loginToken;
        this.exID = exID;
        this.openId = openId;
        this.grant_type = grant_type;
        this.requestListener = requestListener;
    }

    @Override
    public void run() {
        try {
            new LoginRequest(context, code, mobile, loginToken, exID, openId, grant_type).execute(true);

            boolean isThirdLogin = !TextUtils.isEmpty(openId);
            new GetUserInfoRequest(context, isThirdLogin).schedule(false, requestListener);
        } catch (Exception e) {
            if(requestListener != null) {
                requestListener.onFailed(e);
            }
            Log.e(TAG, e.getMessage());
        }

    }
}
