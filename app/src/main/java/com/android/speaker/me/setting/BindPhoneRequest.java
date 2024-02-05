package com.android.speaker.me.setting;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 绑定手机号
 */
public class BindPhoneRequest extends BaseRequest<Boolean> {
    private String mobile;
    private String code;
    public BindPhoneRequest(Context context, String mobile, String code) {
        super(context);
        this.mobile = mobile;
        this.code = code;
    }
    @Override
    protected String url() {
        return UrlManager.BIND_PHONE;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("mobile", mobile);
        obj.put("code", code);
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {

        return true;
    }
}
