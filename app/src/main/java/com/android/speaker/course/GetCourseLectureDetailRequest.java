package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 课程精讲详情
 */
public class GetCourseLectureDetailRequest extends BaseRequest<CourseLectureDetail> {
    private String id;
    public GetCourseLectureDetailRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_SCENE_PROJECT_DETAIL;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected CourseLectureDetail result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");

        return new CourseLectureDetail().parse(data);
    }
}
