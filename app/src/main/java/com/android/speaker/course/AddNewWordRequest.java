package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/***
 * 添加生词本
 */
public class AddNewWordRequest extends BaseRequest<String> {
    // 单词ID
    String id;
    public AddNewWordRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {

        return UrlManager.ADD_NEW_WORD;
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
