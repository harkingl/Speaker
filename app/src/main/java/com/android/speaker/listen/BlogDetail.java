package com.android.speaker.listen;

import com.android.speaker.course.AnalysisItem;
import com.android.speaker.course.CourseLectureDetail;

import org.json.JSONObject;

import java.util.List;

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
    public List<AnalysisItem> list;

    public BlogDetail parse(JSONObject obj) {
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

        return this;
    }
}
