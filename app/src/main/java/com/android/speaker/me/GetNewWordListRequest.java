package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.course.WordInfo;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 获取生词列表
 */
public class GetNewWordListRequest extends BaseRequest<List<WordInfo>> {
    private int pageNum;
    private int pageSize;
    public GetNewWordListRequest(Context context, int pageNum, int pageSize) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_NEW_WORD_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected List<WordInfo> result(JSONObject json) throws Exception {
        List<WordInfo> list = new ArrayList<>();
        JSONArray array = json.optJSONArray("data");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new WordInfo().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
