package com.android.speaker.login;

import android.content.Context;

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

public class GetUserInfoRequest extends BaseRequest<UserInfo> {
    public GetUserInfoRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_USER_INFO;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected UserInfo result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        UserInfo info = UserInfo.getInstance();
        if(data != null) {
            info.setAvatar(data.optString("avatarUrl"));
            info.setName(data.optString("nickName"));
            info.storeUserInfo();
        }
        return info;
    }
}
