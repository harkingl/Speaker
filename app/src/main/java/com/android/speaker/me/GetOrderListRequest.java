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
 * 获取订单列表
 */
public class GetOrderListRequest extends BaseRequest<List<OrderInfo>> {
    private int pageNum;
    private int pageSize;
    public GetOrderListRequest(Context context, int pageNum, int pageSize) {
        super(context);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_ORDER_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected List<OrderInfo> result(JSONObject json) throws Exception {
        List<OrderInfo> list = new ArrayList<>();
        JSONObject dataObj = json.optJSONObject("data");
        JSONArray array = dataObj.optJSONArray("list");
        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new OrderInfo().parse(array.getJSONObject(i)));
            }
        }

        return list;
    }
}
