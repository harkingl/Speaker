package com.android.speaker.course;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoursePreviewInfo {
    public String id;
    public String title;
    public String des;
    public String homePage;
    // 0免费，1vip
    public int type;
    public String leveName;
    public List<String> words;
    public String sceneSpeak;
    public String sceneProject;
    public String openSpeak;
    public String[] tips;

    public CoursePreviewInfo parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        title = obj.optString("title");
        des = obj.optString("des");
        homePage = obj.optString("homePage");
        type = obj.optInt("type");
        leveName = obj.optString("leveName");
        JSONArray wordsArray = obj.optJSONArray("words");
        if(wordsArray != null && wordsArray.length() > 0) {
            words = new ArrayList<>();
            for(int i = 0; i < wordsArray.length(); i++) {
                words.add(wordsArray.getString(i));
            }
        }
        sceneSpeak = obj.optString("sceneSpeak");
        sceneProject = obj.optString("sceneProject");
        openSpeak = obj.optString("openSpeak");
        String tipsStr = obj.optString("tips");
        if(!TextUtils.isEmpty(tipsStr)) {
            tips = tipsStr.split(",");
        }

        return this;
    }
}
