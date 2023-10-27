package com.android.speaker.favorite;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 收藏场景连播
 */
public class AddStreamFavoriteRequest extends BaseRequest<String> {
    // 博客ID
    private String id;
    public AddStreamFavoriteRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.ADD_STREAM_FAVORITE;
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
