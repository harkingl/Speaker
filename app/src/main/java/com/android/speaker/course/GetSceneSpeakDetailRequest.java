package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 场景对话详情
 */
public class GetSceneSpeakDetailRequest extends BaseRequest<SceneSpeakDetail> {
    private String id;
    public GetSceneSpeakDetailRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_SCENE_SPEAK_DETAIL;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected SceneSpeakDetail result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");

        return new SceneSpeakDetail().parse(data);
    }
}
