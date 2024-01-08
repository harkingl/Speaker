package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 获取对话历史列表
 */
public class GetChatHistoryListRequest extends BaseRequest<List<ChatReportInfo>> {
    private String id;
    private int pageNum;
    private int pageSize;
    public GetChatHistoryListRequest(Context context, String id, int pageNum, int pageSize) {
        super(context);
        this.id = id;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_CHAT_HISTORY_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected List<ChatReportInfo> result(JSONObject json) throws Exception {
        List<ChatReportInfo> list = new ArrayList<>();
        JSONObject dataObj = json.optJSONObject("data");
        JSONArray array = dataObj.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new ChatReportInfo().parseHistory(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
