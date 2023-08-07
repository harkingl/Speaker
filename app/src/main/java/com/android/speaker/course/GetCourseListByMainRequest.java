package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 通过主类获取课堂信息
 */
public class GetCourseListByMainRequest extends BaseRequest<List<CourseItem>> {
    private int pageNum;
    private int pageSize;
    private int parentId;
    public GetCourseListByMainRequest(Context context, int pageNum, int pageSize, int parentId) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.parentId = parentId;
    }
    @Override
    protected String url() {
        return UrlManager.GET_COURSE_LIST_BY_MAIN;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        obj.put("parentId", parentId);
        return obj.toString();
    }

    @Override
    protected List<CourseItem> result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        List<CourseItem> list = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new CourseItem().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
