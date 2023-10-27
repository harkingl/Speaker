package com.android.speaker.listen;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 根据分类ID获取分类场景信息
 */
public class GetScenePlayDataListRequest extends BaseRequest<List<ScenePlayDataItem>> {
    private int pageNum;
    private int pageSize;
    private String scenePlayId;
    public GetScenePlayDataListRequest(Context context, int pageNum, int pageSize, String scenePlayId) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.scenePlayId = scenePlayId;
    }
    @Override
    protected String url() {
        return UrlManager.GET_SCENE_PLAY_DATA_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        obj.put("scenePlayId", scenePlayId);
        return obj.toString();
    }

    @Override
    protected List<ScenePlayDataItem> result(JSONObject json) throws Exception {
        List<ScenePlayDataItem> list = new ArrayList<>();
        JSONArray array = json.optJSONArray("data");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new ScenePlayDataItem().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
