package com.android.speaker.me;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/***
 * 等级信息
 */
public class LevelInfo implements Serializable {
    public String level;
    public String name;
    public String levelDes;

    public LevelInfo parse(JSONObject obj) throws JSONException {
        this.level = obj.optString("level");
        this.name = obj.optString("name");
        this.levelDes = obj.optString("levelDes");

        return this;
    }
}
