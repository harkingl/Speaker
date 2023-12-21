package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/***
 * 测试结果
 */
public class QueryTestResultRequest extends BaseRequest<LevelInfo> {
    private Map map;
    public QueryTestResultRequest(Context context, Map map) {
        super(context);
        this.map = map;
    }
    @Override
    protected String url() {
        return UrlManager.QUERY_TEST_RESULT;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        if(map != null) {
            Iterator<String> iterator = map.keySet().iterator();
            while(iterator.hasNext()) {
                String id = iterator.next();
                obj.put(id, map.get(id));
            }
        }
        return obj.toString();
    }

    @Override
    protected LevelInfo result(JSONObject json) throws Exception {

        return new LevelInfo().parse(json.optJSONObject("data"));
    }
}
