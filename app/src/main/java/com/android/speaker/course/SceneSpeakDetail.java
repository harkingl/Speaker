package com.android.speaker.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SceneSpeakDetail implements Serializable {
    public String id;
    public String audioSssKey;
    public String report;
    public String translation;
    public List<SpeakItem> speakItemList;
    public List<HeaderContent> headerContentList;

    public SceneSpeakDetail parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        audioSssKey = obj.optString("audioSssKey");
        JSONObject contentObj = obj.getJSONObject("content");
        JSONArray analysisArray = contentObj.optJSONArray("content");
        if(analysisArray != null) {
            speakItemList = new ArrayList<>();
            for(int i = 0; i < analysisArray.length(); i++) {
                SpeakItem item = new SpeakItem();
                item.name = analysisArray.getJSONObject(i).optString("name");
                item.icon = analysisArray.getJSONObject(i).optString("icon");
                item.content = analysisArray.getJSONObject(i).optString("content");
                item.transfer = analysisArray.getJSONObject(i).optString("transfer");
                item.teacherInsights = analysisArray.getJSONObject(i).optString("teacherInsights");
                item.startTime = analysisArray.getJSONObject(i).optDouble("startTime");
                item.endTime = analysisArray.getJSONObject(i).optDouble("endTime");
                speakItemList.add(item);
            }
        }
        JSONArray headContentArray = contentObj.optJSONArray("headerContent");
        if(headContentArray != null) {
            headerContentList = new ArrayList<>();
            for(int i = 0; i < headContentArray.length(); i++) {
                HeaderContent item = new HeaderContent();
                item.context = headContentArray.getJSONObject(i).optString("context");
                item.startTime = headContentArray.getJSONObject(i).optDouble("startTime");
                item.endTime = headContentArray.getJSONObject(i).optDouble("endTime");
                headerContentList.add(item);
            }
        }

        return this;
    }

    public class SpeakItem {
        public String name;
        public String icon;
        public String content;
        public String transfer;
        public String teacherInsights;
        public double startTime;
        public double endTime;
    }

    public class HeaderContent {
        public String context;
        public double startTime;
        public double endTime;
    }
}
