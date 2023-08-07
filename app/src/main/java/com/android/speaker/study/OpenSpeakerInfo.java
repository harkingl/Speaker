package com.android.speaker.study;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenSpeakerInfo {
    // 场景ID
    public int scenesId;
    public String tips;
    public String ossUrl;
    public String title;

    public OpenSpeakerInfo parse(JSONObject obj) throws JSONException {
        scenesId = obj.optInt("scenesId");
        tips = obj.optString("tips");
        ossUrl = obj.optString("ossUrl");
        title = obj.optString("title");

        return this;
    }
}
