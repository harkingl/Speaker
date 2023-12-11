package com.android.speaker.me.vip;

import android.content.Context;
import android.text.TextUtils;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 生成订单号
 */
public class PayOrderRequest extends BaseRequest<PayInfo> {
    public static final int PAY_TYPE_ALI = 0;
    public static final int PAY_TYPE_WX = 1;
    private int payType;
    private String productId;
    private String couPonId;

    public PayOrderRequest(Context context, int payType, String productId, String couPonId) {
        super(context);

        this.payType = payType;
        this.productId = productId;
        this.couPonId = couPonId;
    }
    @Override
    protected String url() {
        return UrlManager.PAY_ORDER;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("payType", payType);
        obj.put("productId", productId);
        if(!TextUtils.isEmpty(couPonId)) {
            obj.put("couPonId", couPonId);
        }
        return obj.toString();
    }

    @Override
    protected PayInfo result(JSONObject json) throws Exception {
        if(payType == PAY_TYPE_ALI) {
            PayInfo info = new PayInfo();
            info.orderInfo = json.optString("data");
            return info;
        }

        return new PayInfo().parse(json.optJSONObject("data"));
    }
}
