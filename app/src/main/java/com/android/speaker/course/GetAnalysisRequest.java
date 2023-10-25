package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

/***
 * 获取文本点评和解析
 */
public class GetAnalysisRequest extends BaseRequest<ChatItem> {
    private List<String> list;
    private ChatItem chatItem;
    public GetAnalysisRequest(Context context, List<String> list, ChatItem chatItem) {
        super(context);
        this.list = list;
        this.chatItem = chatItem;
    }
    @Override
    protected String url() {
        return UrlManager.GET_CONTENT_ANALYSIS;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for(String content : list) {
            array.put(content);
        }
//        array.put("Good morning! How are you today?");
//        array.put("How about you?");
        obj.put("content", array);
        return obj.toString();
    }

    @Override
    protected ChatItem result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        if(chatItem == null) {
            chatItem = new ChatItem();
        }
        chatItem.score = data.optString("score");
        chatItem.analysis = data.optString("analysis");
        chatItem.standarAnswer = data.optString("standarAnswer");

        return chatItem;
    }
}
