package com.android.speaker.course;

import android.content.Context;
import android.text.TextUtils;

import com.android.speaker.me.ChatReportInfo;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/***
 * 对话报告
 */
public class GetChatReportRequest extends BaseRequest<ChatReportInfo> {
    private String id;
    private List<String> list;
    private String chatData;
    private String title;

    public GetChatReportRequest(Context context, String id, List<String> list, String chatData, String title) {
        super(context);

        this.id = id;
        this.list = list;
        this.chatData = chatData;
        this.title = title;
    }
    @Override
    protected String url() {
        return UrlManager.GET_CHAT_REPORT;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        JSONArray array = new JSONArray();
        for(String content : list) {
            array.put(content);
        }
        obj.put("content", array);
        obj.put("chatData", chatData);
        if(!TextUtils.isEmpty(title)) {
            obj.put("title", title);
        }
        return obj.toString();
    }

    @Override
    protected ChatReportInfo result(JSONObject json) throws Exception {

        return new ChatReportInfo().parse(json.optJSONObject("data"));
    }
}
