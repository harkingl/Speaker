package com.android.speaker.course;

import org.json.JSONObject;

import java.io.Serializable;

public class CourseItem implements Serializable {
    public String id;
    public String title;
    public String des;
    public String homePage;
    // 0免费，1vip
    public int type;
    public String leveName;

    public CourseItem parse(JSONObject obj) {
        id = obj.optString("id");
        title = obj.optString("title");
        des = obj.optString("des");
        homePage = obj.optString("homePage");
        type = obj.optInt("type");
        leveName = obj.optString("leveName");

        return this;
    }
}
