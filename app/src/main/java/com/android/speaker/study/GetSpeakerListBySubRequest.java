package com.android.speaker.study;

import android.content.Context;

import com.android.speaker.base.bean.PagedListEntity;
import com.android.speaker.course.CourseItem;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 通过子类获取开口说列表
 */
public class GetSpeakerListBySubRequest extends BaseRequest<PagedListEntity<OpenSpeakerInfo>> {
    private int pageNum;
    private int pageSize;
    private int subId;
    public GetSpeakerListBySubRequest(Context context, int pageNum, int pageSize, int subId) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.subId = subId;
    }
    @Override
    protected String url() {
        return UrlManager.GET_OPEN_SPEAKER_LIST_BY_SUB;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        obj.put("subId", subId);
        return obj.toString();
    }

    @Override
    protected PagedListEntity<OpenSpeakerInfo> result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        List<OpenSpeakerInfo> list = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new OpenSpeakerInfo().parse(array.getJSONObject(i)));
            }
        }
        PagedListEntity entity = new PagedListEntity();
        entity.setList(list);
        int count = data.optInt("total", list.size());
        int pageCount = count%pageSize == 0 ? count/pageSize : count/pageSize+1;
        entity.setRecordCount(count);
        entity.setPageCount(pageCount);

        return entity;
    }
}
