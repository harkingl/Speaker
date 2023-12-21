package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 设置目标时间
 */
public class SetTargetTimeRequest extends BaseRequest<Boolean> {
    private int targetTime;
    public SetTargetTimeRequest(Context context, int targetTime) {
        super(context);
        this.targetTime = targetTime;
    }
    @Override
    protected String url() {
        return UrlManager.SET_TARGET_TIME;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("targetTime", targetTime);
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {

        return true;
    }
}
