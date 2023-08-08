package com.android.speaker.listen;

import org.json.JSONObject;

/***
 * 节目精选item
 */
public class ProgramItem {
    public String id;
    public String iconUrl;
    public String title;
    public String des;

    public ProgramItem parse(JSONObject obj) {
        id = obj.optString("id");
        iconUrl = obj.optString("iconUrl");
        title = obj.optString("title");
        des = obj.optString("des");

        return this;
    }
}
