package com.android.speaker.listen;

import org.json.JSONObject;

/***
 * 博客详情
 */
public class BlogDetail {
    public String author;
    public String category;
    public String copyright;
    public String dataSource;
    public String des;
    public int favoriteCount;
    public String iconUrl;
    public String id;
    public int isDeleted;
    public boolean isVip;
    public int likeCount;
    public String name;
    public String website;

    public BlogDetail parse(JSONObject obj) {
        author = obj.optString("author");
        category = obj.optString("category");
        copyright = obj.optString("copyright");
        dataSource = obj.optString("dataSource");
        des = obj.optString("des");
        favoriteCount = obj.optInt("favoriteCount");
        iconUrl = obj.optString("iconUrl");
        id = obj.optString("id");
        isDeleted = obj.optInt("isDeleted");
        isVip = obj.optBoolean("isVip");
        likeCount = obj.optInt("likeCount");
        name = obj.optString("name");
        website = obj.optString("website");

        return this;
    }
}
