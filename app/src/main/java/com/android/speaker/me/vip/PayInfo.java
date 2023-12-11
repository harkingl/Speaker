package com.android.speaker.me.vip;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 支付订单信息
 */
public class PayInfo {
    public String orderInfo;
    public String appId;
    public String partnerId;
    public String prepayId;
    public String packageVal;
    public String nonceStr;
    public String timestamp;
    public String sign;

    public PayInfo parse(JSONObject obj) throws JSONException {
        this.appId = obj.optString("appid");
        this.partnerId = obj.optString("partnerId");
        this.prepayId = obj.optString("prepayId");
        this.packageVal = obj.optString("packageVal");
        this.nonceStr = obj.optString("nonceStr");
        this.timestamp = obj.optString("timestamp");
        this.sign = obj.optString("sign");

        return this;
    }
}
