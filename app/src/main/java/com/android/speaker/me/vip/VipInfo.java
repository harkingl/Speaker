package com.android.speaker.me.vip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 商品和优惠券信息
 */
public class VipInfo {
    private static final String PAY_TYPE_ALI = "0";
    private static final String PAY_TYPE_WX = "1";
    List<ProductInfo> aliProductList;
    List<ProductInfo> wxProductList;
    List<CouponInfo> couponList;

    public VipInfo parse(JSONObject obj) throws JSONException {
        JSONArray productArray = obj.optJSONArray("products");
        aliProductList = new ArrayList<>();
        wxProductList = new ArrayList<>();
        if(productArray != null) {
            for(int i = 0; i < productArray.length(); i++) {
                ProductInfo info = new ProductInfo().parse(productArray.getJSONObject(i));
                if(info.payType.contains(PAY_TYPE_ALI)) {
                    aliProductList.add(info);
                }
                if(info.payType.contains(PAY_TYPE_WX)) {
                    wxProductList.add(info);
                }
            }
        }
        JSONArray couponArray = obj.optJSONArray("coupons");
        couponList = new ArrayList<>();
        if(couponArray != null) {
            for(int i = 0; i < couponArray.length(); i++) {
                couponList.add(new CouponInfo().parse(couponArray.getJSONObject(i)));
            }
        }

        return this;
    }
}
