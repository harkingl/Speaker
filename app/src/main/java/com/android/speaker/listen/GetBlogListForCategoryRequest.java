package com.android.speaker.listen;

import android.content.Context;

import com.android.speaker.base.bean.PagedListEntity;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 博客分页数据
 */
public class GetBlogListForCategoryRequest extends BaseRequest<PagedListEntity<BlogItem>> {
    public String id;
    private int pageNum;
    private int pageSize;
    public GetBlogListForCategoryRequest(Context context, String id, int pageNum, int pageSize) {
        super(context);
        this.id = id;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_BLOG_LIST_FOR_CATEGORY;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected PagedListEntity result(JSONObject json) throws Exception {
        PagedListEntity entity = new PagedListEntity();
        JSONObject data = json.optJSONObject("data");
        List<BlogItem> list = new ArrayList<>();
        JSONArray array = data.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new BlogItem().parse(array.getJSONObject(i)));
            }
        }
        entity.setList(list);
        int count = data.optInt("total", list.size());
        int pageCount = count%pageSize == 0 ? count/pageSize : count/pageSize+1;
        entity.setRecordCount(count);
        entity.setPageCount(pageCount);

        return entity;
    }
}
