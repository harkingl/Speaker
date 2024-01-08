package com.android.speaker.study;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpeakerDetailInfo implements Serializable {
    public String id;
    public String scenesId;
    public String sceneDes;
    public String tips;
    public String ossUrl;
    public String title;
    public List<ExampleInfo> exampleInfoList;

    public SpeakerDetailInfo parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        scenesId = obj.optString("scenesId");
        sceneDes = obj.optString("sceneDess");
        tips = obj.optString("tips");
        ossUrl = obj.optString("ossUrl");
        title = obj.optString("title");
        JSONArray exampleInfos = obj.optJSONArray("exampleInfos");
        if(exampleInfos != null) {
            exampleInfoList = new ArrayList<>();
            for(int i = 0; i < exampleInfos.length(); i++) {
                ExampleInfo info = new ExampleInfo();
                info.titleEn = exampleInfos.getJSONObject(i).optString("en");
                info.titleZh = exampleInfos.getJSONObject(i).optString("zh");
                exampleInfoList.add(info);
            }
        }

        return this;
    }
}
