package com.android.speaker.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpeakChatDetail implements Serializable {
    public String id;
    public String userName;
    public String myName;
    public List<ChatItem> chatItemList;
    // 顶部滚动内容列表
    public List<HeaderContent> scrollTitleList;
    // 弹框提示信息
    public List<String> showTitles;

    public SpeakChatDetail parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
//        JSONObject contentObj = obj.getJSONObject("content");
//        JSONArray analysisArray = contentObj.optJSONArray("content");
//        if(analysisArray != null) {
//            chatItemList = new ArrayList<>();
//            for(int i = 0; i < analysisArray.length(); i++) {
//                ChatItem item = new ChatItem();
//                item.name = analysisArray.getJSONObject(i).optString("name");
//                item.icon = analysisArray.getJSONObject(i).optString("icon");
//                item.content = analysisArray.getJSONObject(i).optString("content");
//                item.audioOssKey = analysisArray.getJSONObject(i).optString("audioOssKey");
//                item.transfer = analysisArray.getJSONObject(i).optString("transfer");
//                chatItemList.add(item);
//            }
//        }
        JSONArray scrollTitleArray = obj.optJSONArray("scrollTitleList");
        if(scrollTitleArray != null) {
            scrollTitleList = new ArrayList<>();
            for(int i = 0; i < scrollTitleArray.length(); i++) {
                HeaderContent item = new HeaderContent();
                item.context = scrollTitleArray.getString(i);
//                item.startTime = scrollTitleArray.getJSONObject(i).optDouble("startTime");
//                item.endTime = scrollTitleArray.getJSONObject(i).optDouble("endTime");
                scrollTitleList.add(item);
            }
        }
        JSONArray showTitleArray = obj.optJSONArray("showTitles");
        if(showTitleArray != null) {
            showTitles = new ArrayList<>();
            for(int i = 0; i < showTitleArray.length(); i++) {
                showTitles.add(showTitleArray.getString(i));
            }
        }

        return this;
    }

    public class HeaderContent {
        public String context;
        public double startTime;
        public double endTime;
    }
}
