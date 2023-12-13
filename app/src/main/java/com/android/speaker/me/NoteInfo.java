package com.android.speaker.me;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/***
 * 笔记信息
 */
public class NoteInfo implements Serializable {
    public String id;
    public String title;
    public String content;

    public NoteInfo parse(JSONObject obj) throws JSONException {
        this.id = obj.optString("id");
        this.title = obj.optString("title");
        this.content = obj.optString("content");

        return this;
    }
}
