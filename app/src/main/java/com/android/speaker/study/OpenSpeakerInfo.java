package com.android.speaker.study;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpenSpeakerInfo implements Serializable {
    // 场景ID
    public String id;
    public String scenesId;
    public String tips;
    public String ossUrl;
    public String title;
    public String des;

    public OpenSpeakerInfo parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        scenesId = obj.optString("scenesId");
        tips = obj.optString("tips");
        ossUrl = obj.optString("ossUrl");
        title = obj.optString("title");
        des = obj.optString("desc");

        return this;
    }
}
