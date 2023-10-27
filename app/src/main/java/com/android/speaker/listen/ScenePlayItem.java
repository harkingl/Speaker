package com.android.speaker.listen;

import org.json.JSONObject;

import java.io.Serializable;

/***
 * 场景连播item
 */
public class ScenePlayItem implements Serializable {
    public String id;
    public String iconUrl;
    public String title;
    public int count;

    public ScenePlayItem parse(JSONObject obj) {
        id = obj.optString("id");
        iconUrl = obj.optString("iconUrl");
        title = obj.optString("title");
        count = obj.optInt("count");

        return this;
    }
}
