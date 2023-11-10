package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/***
 * 移除生词本
 */
public class RemoveNewWordRequest extends BaseRequest<Boolean> {
    // 生词本ID
    private List<String> idList;
    public RemoveNewWordRequest(Context context, List<String> idList) {
        super(context);
        this.idList = idList;
    }
    @Override
    protected String url() {
        return UrlManager.REMOVE_NEW_WORD;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        if(idList != null) {
            JSONArray array = new JSONArray();
            for(String id : idList) {
                array.put(id);
            }
            obj.put("ids", array);
        }
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {

        return true;
    }
}
