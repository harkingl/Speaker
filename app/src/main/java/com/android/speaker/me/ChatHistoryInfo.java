package com.android.speaker.me;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/***
 * 对话历史
 */
public class ChatHistoryInfo implements Serializable {
    public String id;

    public ChatHistoryInfo parse(JSONObject obj) throws JSONException {
        this.id = obj.optString("id");

        return this;
    }
}
