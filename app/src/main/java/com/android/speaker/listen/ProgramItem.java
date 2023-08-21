package com.android.speaker.listen;

import org.json.JSONObject;

import java.io.Serializable;

/***
 * 节目精选item
 */
public class ProgramItem implements Serializable {
    public String id;
    public String iconUrl;
    public String title;
    public String des;
    public String category;
    public String author;

    public ProgramItem parse(JSONObject obj) {
        id = obj.optString("id");
        iconUrl = obj.optString("iconUrl");
        title = obj.optString("title");
        des = obj.optString("des");
        category = obj.optString("category");
        author = obj.optString("author");

        return this;
    }
}
