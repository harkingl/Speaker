package com.android.speaker.me.setting;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 绑定微信
 */
public class BindWxRequest extends BaseRequest<Boolean> {
    private String accessToken;
    private String openId;
    public BindWxRequest(Context context, String accessToken, String openId) {
        super(context);
        this.accessToken = accessToken;
        this.openId = openId;
    }
    @Override
    protected String url() {
        return UrlManager.BIND_WX;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("accessToken", accessToken);
        obj.put("openId", openId);
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {

        return true;
    }
}
