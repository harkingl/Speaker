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
    private List<String> idList;
    public AddNewWordRequest(Context context, List<String> idList) {
        super(context);
        this.idList = idList;
    }
    @Override
    protected String url() {

        return UrlManager.ADD_NEW_WORD;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        if(idList != null) {
            JSONArray array = new JSONArray();
            for(String id : idList) {
                array.put(id);
            }
            obj.put("noteBooks", array);
        }

        return obj.toString();
    }

    @Override
    protected String result(JSONObject json) throws Exception {

        return json.optString("data");
    }
}
