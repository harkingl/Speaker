package com.android.speaker.login;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 发送验证码
 */
public class GetCaptchaRequest extends BaseRequest<Boolean> {
    private String phoneNumber;
    protected GetCaptchaRequest(Context context, String phoneNumber) {
        super(context);

        this.phoneNumber = phoneNumber;
    }

    @Override
    protected String url() {
        return UrlManager.GET_CAPTCHA;
    }

    @Override
    protected String body() throws JSONException {
        return new JSONObject().put("phoneNumber", phoneNumber).toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {
        return true;
    }
}
