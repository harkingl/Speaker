package com.android.speaker.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseLectureDetail implements Serializable {
    public String id;
    public String audioSssKey;
    public String report;
    public String translation;
    public List<AnalysisItem> analysisItemList;
    public List<HeaderContent> headerContentList;

    public CourseLectureDetail parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        audioSssKey = obj.optString("audioSssKey");
        JSONObject contentObj = obj.getJSONObject("content");
        report = contentObj.optString("report");
        translation = contentObj.optString("translation");
        JSONArray analysisArray = contentObj.optJSONArray("analysis");
        if(analysisArray != null) {
            analysisItemList = new ArrayList<>();
            for(int i = 0; i < analysisArray.length(); i++) {
                AnalysisItem item = new AnalysisItem();
                item.sentence = analysisArray.getJSONObject(i).optString("sentence");
                item.grammar = analysisArray.getJSONObject(i).optString("grammar");
                item.translation = analysisArray.getJSONObject(i).optString("translation");
                item.startTime = analysisArray.getJSONObject(i).optDouble("startTime");
                item.endTime = analysisArray.getJSONObject(i).optDouble("endTime");
                analysisItemList.add(item);
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

    public class AnalysisItem {
        public String sentence;
        public String grammar;
        public String translation;
        public double startTime;
        public double endTime;
    }

    public class HeaderContent {
        public String context;
        public double startTime;
        public double endTime;
    }
}
