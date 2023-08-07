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
 * 获取首页精品课程推荐
 */
public class GetRecommendCourseListRequest extends BaseRequest<List<CourseItem>> {
    public GetRecommendCourseListRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_RECOMMEND_COURSE_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
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
