package com.android.speaker.listen;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 场景直播分页数据
 */
public class GetProgramListRequest extends BaseRequest<List<ProgramItem>> {
    private int pageNum;
    private int pageSize;
    public GetProgramListRequest(Context context, int pageNum, int pageSize) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_FEATURED_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected List<ProgramItem> result(JSONObject json) throws Exception {
        JSONArray array = json.optJSONArray("data");
        List<ProgramItem> list = new ArrayList<>();
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new ProgramItem().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
