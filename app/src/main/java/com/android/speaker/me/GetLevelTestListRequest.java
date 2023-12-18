package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 获取等级测试列表
 */
public class GetLevelTestListRequest extends BaseRequest<List<LevelQuestionInfo>> {
    public GetLevelTestListRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_LEVEL_TEST_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected List<LevelQuestionInfo> result(JSONObject json) throws Exception {
        List<LevelQuestionInfo> list = new ArrayList<>();
//        JSONObject dataObj = json.optJSONObject("data");
//        JSONArray array = dataObj.optJSONArray("list");
        JSONArray array = json.optJSONArray("data");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new LevelQuestionInfo().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
