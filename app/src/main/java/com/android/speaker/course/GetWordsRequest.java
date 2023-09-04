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
 * 精品课程/课程预览
 */
public class GetWordsRequest extends BaseRequest<List<WordInfo>> {
    private String id;
    public GetWordsRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_WORDS;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected List<WordInfo> result(JSONObject json) throws Exception {
        JSONArray data = json.optJSONArray("data");
        List<WordInfo> list = new ArrayList<>();
        if(data != null) {
            for(int i = 0; i < data.length(); i++) {
                list.add(new WordInfo().parse(data.getJSONObject(i)));
            }
        }

        return list;
    }
}
