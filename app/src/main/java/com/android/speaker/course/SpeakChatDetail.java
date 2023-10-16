package com.android.speaker.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpeakChatDetail implements Serializable {
    public String id;
    public String userName;
    public String myName;
    public List<ChatItem> chatItemList;
    public List<HeaderContent> headerContentList;

    public SpeakChatDetail parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        JSONObject contentObj = obj.getJSONObject("content");
        JSONArray analysisArray = contentObj.optJSONArray("content");
        if(analysisArray != null) {
            chatItemList = new ArrayList<>();
            for(int i = 0; i < analysisArray.length(); i++) {
                ChatItem item = new ChatItem();
                item.name = analysisArray.getJSONObject(i).optString("name");
                item.icon = analysisArray.getJSONObject(i).optString("icon");
                item.content = analysisArray.getJSONObject(i).optString("content");
                item.audioOssKey = analysisArray.getJSONObject(i).optString("audioOssKey");
                item.transfer = analysisArray.getJSONObject(i).optString("transfer");
                item.teacherInsights = analysisArray.getJSONObject(i).optString("teacherInsights");
                item.startTime = analysisArray.getJSONObject(i).optDouble("startTime");
                item.endTime = analysisArray.getJSONObject(i).optDouble("endTime");
                chatItemList.add(item);
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

    public class HeaderContent {
        public String context;
        public double startTime;
        public double endTime;
    }
}
