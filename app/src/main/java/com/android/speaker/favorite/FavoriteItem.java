package com.android.speaker.favorite;

import com.android.speaker.listen.BlogItem;

import org.json.JSONObject;

import java.io.Serializable;

public class FavoriteItem implements Serializable {
    public static final int TYPE_SCENE = 0;
    public static final int TYPE_STREAM = 1;
    public static final int TYPE_BLOG = 2;
    public static final int TYPE_QUALITY = 3;
    public static final int TYPE_SPECIAL = 4;
    public String favoriteId;
    public String date;
    public String name;
    public String id;
    // 0 场景课程 1 场景连播 2 英语博客 3 精品课程 4 专项课程
    public int type;

    public FavoriteItem parse(JSONObject obj) {
        this.favoriteId = obj.optString("favoriteId");
        this.date = obj.optString("data");
        this.name = obj.optString("name");
        this.id = obj.optString("id");
        this.type = obj.optInt("type");

        return this;
    }
}
