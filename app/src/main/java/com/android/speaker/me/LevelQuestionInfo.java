package com.android.speaker.me;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevelQuestionInfo implements Serializable {
    public static final int TYPE_SELECT = 1;
    public static final int TYPE_FILL = 2;
    public static final int TYPE_AUDIO = 3;
    public String id;
    public List<String> selectList;
    // 1 选择  2 填空 3 语音
    public int type;
    // 语音url
    public String audioUrl;
    public String answer;
    public String title;

    public LevelQuestionInfo parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        JSONArray array = obj.optJSONArray("selects");
        if(array != null) {
            selectList = new ArrayList<>();
            for(int i = 0; i < array.length(); i++) {
                selectList.add(array.getString(i));
            }
        }
        type = obj.optInt("type");
        audioUrl = obj.optString("audio_oss_key");
        answer = obj.optString("answer");
        title = obj.optString("title");

        return this;
    }
}
