package com.android.speaker.listen;

import org.json.JSONObject;

/***
 * 博客
 */
public class BlogItem {
    public String id;
    public String iconUrl;
    public int favoriteCount;
    public int likeCount;
    public String titleEn;
    public String titleZh;
    public String publishTime;
    public int audioDuration;

    public BlogItem parse(JSONObject obj) {
        id = obj.optString("id");
        iconUrl = obj.optString("iconUrl");
        favoriteCount = obj.optInt("favoriteCount");
        likeCount = obj.optInt("likeCount");
        titleEn = obj.optString("titleEn");
        titleZh = obj.optString("titleZh");
        publishTime = obj.optString("publishTime");
        audioDuration = obj.optInt("audioDuration");

        return this;
    }
}
