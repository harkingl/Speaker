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
 * 获取问题列表
 */
public class GetQuestionListRequest extends BaseRequest<List<QuestionInfo>> {
    private String id;
    public GetQuestionListRequest(Context context, String id) {
        super(context);
        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_QUESTIONS;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected List<QuestionInfo> result(JSONObject json) throws Exception {
        JSONArray array = json.optJSONArray("data");
        List<QuestionInfo> list = new ArrayList<>();
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new QuestionInfo().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
