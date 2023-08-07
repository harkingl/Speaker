package com.android.speaker.course;

import org.json.JSONObject;

import java.util.List;

public class CategoryItem {
    public int id;
    public String name;
    public String imageUrl;
    public List<CategoryItem> subList;

    public CategoryItem parse(JSONObject obj) {
        id = obj.optInt("id");
        name = obj.optString("name");
        imageUrl = obj.optString("imageUrl");

        return this;
    }
}
