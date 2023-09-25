package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 收藏场景课程
 */
public class AddProjectRequest extends BaseRequest<Boolean> {
    // 博客ID
    private String id;
    public AddProjectRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.ADD_BLOG;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");

        return true;
    }
}
