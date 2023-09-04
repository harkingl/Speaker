package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 精品课程/课程预览
 */
public class GetCoursePreviewRequest extends BaseRequest<CoursePreviewInfo> {
    private String id;
    public GetCoursePreviewRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.COURSE_PREVIEW;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected CoursePreviewInfo result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");

        return new CoursePreviewInfo().parse(data);
    }
}
