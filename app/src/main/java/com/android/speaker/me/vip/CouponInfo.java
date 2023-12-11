package com.android.speaker.me.vip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 优惠券信息
 */
public class CouponInfo {
    public String id;
    public String name;
    public String code;
    public int faceValueType;
    public double faceValue;
    public String validityBeginTime;
    public String validityEndTime;
    public String remark;
    public List<String> products;

    public CouponInfo parse(JSONObject obj) throws JSONException {
        this.id = obj.optString("id");
        this.name = obj.optString("name");
        this.code = obj.optString("code");
        this.faceValueType = obj.optInt("faceValueType");
        this.faceValue = obj.optDouble("faceValue");
        this.validityBeginTime = obj.optString("validityBeginTime");
        this.validityEndTime = obj.optString("validityEndTime");
        this.remark = obj.optString("remark");
        this.products = new ArrayList<>();
        JSONArray array = obj.optJSONArray("products");
        if(array != null) {
            for(int i = 0; i < array.length(); i++) {
                this.products.add(array.getString(i));
            }
        }

        return this;
    }
}
