package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 开口说对话详情
 */
public class GetSpeakChatDetailRequest extends BaseRequest<SpeakChatDetail> {
    private String id;
    public GetSpeakChatDetailRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_SPEAK_CHAT_DETAIL;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected SpeakChatDetail result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");

        return new SpeakChatDetail().parse(data);
    }
}
