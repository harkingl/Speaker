package com.android.speaker.me;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/***
 * 学习记录
 */
public class RecordInfo implements Serializable {
    public String id;
    public long date;
    public String des;
    public String title;
    public String type;

    public RecordInfo parse(JSONObject obj) throws JSONException {
        this.id = obj.optString("id");
        this.date = obj.optLong("date");
        this.des = obj.optString("des");
        this.title = obj.optString("title");
        this.type = obj.optString("type");

        return this;
    }
}
