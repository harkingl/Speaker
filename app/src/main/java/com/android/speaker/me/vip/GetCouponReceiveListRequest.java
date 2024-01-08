package com.android.speaker.me.vip;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 课程抵扣卷（是否领取列表）
 */
public class GetCouponReceiveListRequest extends BaseRequest<List<CouponInfo>> {
    public GetCouponReceiveListRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_COUPON_RECEIVE_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected List<CouponInfo> result(JSONObject json) throws Exception {
        JSONArray array = json.optJSONArray("data");
        List<CouponInfo> list = null;
        if(array != null && array.length() > 0) {
            list = new ArrayList<>();
            for(int i = 0; i < array.length(); i++) {
                list.add(new CouponInfo().parse(array.getJSONObject(i)));
            }
        }
        return list;
    }
}
