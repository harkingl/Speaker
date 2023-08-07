package com.android.speaker.study;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 获取学习信息
 */
public class GetLearnInfoRequest extends BaseRequest<LearnInfo> {
    // 验证码
    public GetLearnInfoRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_LEARN_INFO;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected LearnInfo result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        LearnInfo info = new LearnInfo();
        if(data != null) {
            info.parse(data);
        }
        return info;
    }
}
