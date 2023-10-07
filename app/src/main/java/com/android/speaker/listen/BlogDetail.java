package com.android.speaker.listen;

import com.android.speaker.course.AnalysisItem;
import com.android.speaker.course.CourseLectureDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * 博客详情
 */
public class BlogDetail {
    public String id;
    public String iconUrl;
    public int favoriteCount;
    public int likeCount;
    public int isDeleted;
    public boolean isVip;
    public String subtitleFile;
    public String titleEn;
    public String titleZh;
    public String audioUrl;
    public String publishTime;
    public double audioDuration;
    public String des;
    public String name;
    public String favoritesId;
    public String report;
    public String translation;
    public List<AnalysisItem> list;

    public BlogDetail parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        iconUrl = obj.optString("iconUrl");
        favoriteCount = obj.optInt("favoriteCount");
        likeCount = obj.optInt("likeCount");
        isDeleted = obj.optInt("isDeleted");
        isVip = obj.optBoolean("isVip");
        subtitleFile = obj.optString("subtitleFile");
        titleEn = obj.optString("titleEn");
        titleZh = obj.optString("titleZh");
        audioUrl = obj.optString("audioUrl");
        publishTime = obj.optString("publishTime");
        audioDuration = obj.optDouble("audioDuration");
        des = obj.optString("des");
        name = obj.optString("name");
        favoritesId = obj.optString("favoritesId");
        JSONObject subTitleObj = obj.optJSONObject("subtitle");
        if(subTitleObj != null) {
            report = subTitleObj.optString("report");
            translation = subTitleObj.optString("translation");
            JSONArray analysisArray = subTitleObj.optJSONArray("analysis");
            if(analysisArray != null) {
                list = new ArrayList<>();
                for(int i = 0; i < analysisArray.length(); i++) {
                    AnalysisItem item = new AnalysisItem();
                    item.sentence = analysisArray.getJSONObject(i).optString("sentence");
                    item.grammar = analysisArray.getJSONObject(i).optString("grammar");
                    item.translation = analysisArray.getJSONObject(i).optString("translation");
                    item.startTime = analysisArray.getJSONObject(i).optDouble("startTime");
                    item.endTime = analysisArray.getJSONObject(i).optDouble("endTime");
                    list.add(item);
                }
            }
        }

        return this;
    }
}
