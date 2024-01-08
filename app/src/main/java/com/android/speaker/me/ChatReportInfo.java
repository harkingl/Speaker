package com.android.speaker.me;

import android.text.TextUtils;

import com.android.speaker.course.ChatItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/***
 * 对话报告
 */
public class ChatReportInfo implements Serializable {
    public String id;
    public String title;
    public List<String> pointList;
    public ArrayList<ChatItem> chatList;

    public ChatReportInfo parse(JSONObject obj) throws JSONException {
        this.id = obj.optString("id");
        this.title = obj.optString("title");
        JSONArray pointArray = obj.optJSONArray("analysis");
        if(pointArray != null) {
            pointList = new ArrayList<>();
            for(int i = 0; i < pointArray.length(); i++) {
                pointList.add(pointArray.getString(i));
            }
        }
        String chatData = obj.optString("chatData");
        if(!TextUtils.isEmpty(chatData)) {
            Type listType = new TypeToken<List<ChatItem>>(){}.getType();
            chatList = new Gson().fromJson(chatData, listType);
        }

        return this;
    }

    public ChatReportInfo parseHistory(JSONObject obj) throws JSONException {
        this.id = obj.optString("id");
        JSONObject contentObj = obj.optJSONObject("contentMap");
        if(contentObj != null) {
            this.title = contentObj.optString("title");
            JSONArray pointArray = contentObj.optJSONArray("analysis");
            if(pointArray != null) {
                pointList = new ArrayList<>();
                for(int i = 0; i < pointArray.length(); i++) {
                    pointList.add(pointArray.getString(i));
                }
            }
            String chatData = contentObj.optString("chatData");
            if(!TextUtils.isEmpty(chatData)) {
                Type listType = new TypeToken<List<ChatItem>>(){}.getType();
                chatList = new Gson().fromJson(chatData, listType);
            }
        }

        return this;
    }
}
