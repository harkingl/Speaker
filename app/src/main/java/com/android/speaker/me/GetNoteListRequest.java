package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.base.bean.PagedListEntity;
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
public class GetNoteListRequest extends BaseRequest<PagedListEntity<NoteInfo>> {
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
    protected PagedListEntity<NoteInfo> result(JSONObject json) throws Exception {
        List<NoteInfo> list = new ArrayList<>();
        JSONObject dataObj = json.optJSONObject("data");
        JSONArray array = dataObj.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new NoteInfo().parse(array.getJSONObject(i)));
            }
        }
        PagedListEntity entity = new PagedListEntity();
        entity.setList(list);
        int count = dataObj.optInt("total", list.size());
        int pageCount = count%pageSize == 0 ? count/pageSize : count/pageSize+1;
        entity.setRecordCount(count);
        entity.setPageCount(pageCount);

        return entity;
    }
}
