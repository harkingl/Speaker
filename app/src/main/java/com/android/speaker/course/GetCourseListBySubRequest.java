package com.android.speaker.course;

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
 * 通过子类获取课堂信息
 */
public class GetCourseListBySubRequest extends BaseRequest<PagedListEntity<CourseItem>> {
    private int pageNum;
    private int pageSize;
    private int subId;
    public GetCourseListBySubRequest(Context context, int pageNum, int pageSize, int subId) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.subId = subId;
    }
    @Override
    protected String url() {
        return UrlManager.GET_COURSE_LIST_BY_SUB;
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
    protected PagedListEntity<CourseItem> result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        List<CourseItem> list = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new CourseItem().parse(array.getJSONObject(i)));
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
