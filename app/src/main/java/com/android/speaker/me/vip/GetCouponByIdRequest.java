package com.android.speaker.me.vip;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 课程抵扣卷领取
 */
public class GetCouponByIdRequest extends BaseRequest<Boolean> {
    private String id;

    public GetCouponByIdRequest(Context context, String id) {
        super(context);

        this.id = id;
    }
    @Override
    protected String url() {
        return UrlManager.GET_COUPON_BY_ID;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {

        return true;
    }
}
