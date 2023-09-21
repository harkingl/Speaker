package com.android.speaker.listen;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 博客详情
 */
public class GetProgramDetailRequest extends BaseRequest<BlogDetail> {
    private String id;
    public GetProgramDetailRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_ONE_BLOG_ISSUE;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected BlogDetail result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");

        return new BlogDetail().parse(data);
    }
}
