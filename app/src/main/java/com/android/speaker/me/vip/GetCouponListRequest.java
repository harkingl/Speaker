package com.android.speaker.me.vip;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 优惠券列表
 */
public class GetCouponListRequest extends BaseRequest<CouponListEntity> {
    public GetCouponListRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_COUPON_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected CouponListEntity result(JSONObject json) throws Exception {

        return new CouponListEntity().parse(json.optJSONObject("data"));
    }
}
