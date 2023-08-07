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
 * 获取主类目录列表
 */
public class GetCategoryListRequest extends BaseRequest<List<CategoryItem>> {
    public GetCategoryListRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_CATEGORY_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected List<CategoryItem> result(JSONObject json) throws Exception {
        List<CategoryItem> list = new ArrayList<>();
        JSONArray dataArray = json.optJSONArray("data");
        if(dataArray != null) {
            for(int i = 0; i < dataArray.length(); i++) {
                CategoryItem item = new CategoryItem();
                item.id = dataArray.getJSONObject(i).optInt("id");
                item.name = dataArray.getJSONObject(i).optString("name");
                item.imageUrl = dataArray.getJSONObject(i).optString("imageUrl");
                JSONArray array = dataArray.getJSONObject(i).optJSONArray("categoriesList");
                if(array != null && array.length() > 0) {
                    List<CategoryItem> subList = new ArrayList<>();
                    for(int j = 0; j < array.length(); j++) {
                        subList.add(new CategoryItem().parse(array.getJSONObject(j)));
                    }
                    item.subList = subList;
                }
                list.add(item);
            }
        }

        return list;
    }
}
