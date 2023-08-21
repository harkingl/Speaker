package com.android.speaker.listen;

import org.json.JSONObject;

/***
 * 场景连播信息
 */
public class ScenePlayDataItem {
    // 音频文件
    public String audioOssKey;
    public String des;
    public String homePage;
    public int id;
    public String leveName;
    public int odernum;
    public String subtitleOssKey;
    public String tips;
    public String title;
    // 类型,0 免费， 1 vip
    public int type;

    public ScenePlayDataItem parse(JSONObject obj) {
        audioOssKey = obj.optString("audioOssKey");
        des = obj.optString("des");
        homePage = obj.optString("homePage");
        id = obj.optInt("id");
        leveName = obj.optString("leveName");
        odernum = obj.optInt("odernum");
        subtitleOssKey = obj.optString("subtitleOssKey");
        tips = obj.optString("tips");
        title = obj.optString("title");
        type = obj.optInt("type");

        return this;
    }
}
