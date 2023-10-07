package com.android.speaker.listen;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 移除收藏
 */
public class RemoveBlogRequest extends BaseRequest<Boolean> {
    // 收藏ID
    private String id;
    public RemoveBlogRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.REMOVE_BLOG;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {

        return true;
    }
}
