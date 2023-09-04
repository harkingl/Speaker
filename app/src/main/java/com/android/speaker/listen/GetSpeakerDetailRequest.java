package com.android.speaker.listen;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;
import com.android.speaker.study.OpenSpeakerInfo;
import com.android.speaker.study.SpeakerDetailInfo;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 开口说详情
 */
public class GetSpeakerDetailRequest extends BaseRequest<SpeakerDetailInfo> {
    private int id;
    public GetSpeakerDetailRequest(Context context, int id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_OPEN_SPEAKER_DETAIL;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected SpeakerDetailInfo result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");

        return new SpeakerDetailInfo().parse(data);
    }
}
