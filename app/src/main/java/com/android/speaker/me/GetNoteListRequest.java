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
 * 获取笔记列表
 */
public class GetNoteListRequest extends BaseRequest<List<NoteInfo>> {
    private int pageNum;
    private int pageSize;
    public GetNoteListRequest(Context context, int pageNum, int pageSize) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_NOTE_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected List<NoteInfo> result(JSONObject json) throws Exception {
        List<NoteInfo> list = new ArrayList<>();
        JSONObject dataObj = json.optJSONObject("data");
        JSONArray array = dataObj.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new NoteInfo().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
