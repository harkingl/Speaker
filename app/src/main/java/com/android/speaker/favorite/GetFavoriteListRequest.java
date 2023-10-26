package com.android.speaker.favorite;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 获取收藏列表
 */
public class GetFavoriteListRequest extends BaseRequest<List<FavoriteItem>> {
    private int pageNum;
    private int pageSize;
    public GetFavoriteListRequest(Context context, int pageNum, int pageSize) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_FAVORITE_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected List<FavoriteItem> result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        List<FavoriteItem> list = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new FavoriteItem().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
