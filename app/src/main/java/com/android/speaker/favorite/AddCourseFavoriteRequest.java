package com.android.speaker.favorite;

import android.content.Context;

import com.android.speaker.course.CourseUtil;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 收藏课程
 */
public class AddCourseFavoriteRequest extends BaseRequest<String> {
    // 课程ID
    private String id;
    private int type;
    public AddCourseFavoriteRequest(Context context, String id, int type) {
        super(context);
        this.id = id;
        this.type = type;
    }
    @Override
    protected String url() {
        String url = UrlManager.ADD_COURSE_CATALOG;
        if(type == CourseUtil.TYPE_COURSE_PROJECT) {
            url = UrlManager.ADD_COURSE_PROJECT;
        } else if(type == CourseUtil.TYPE_COURSE_SPECIAL) {
            url = UrlManager.ADD_COURSE_SPECIAL;
        }

        return url;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected String result(JSONObject json) throws Exception {

        return json.optString("data");
    }
}
