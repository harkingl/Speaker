package com.android.speaker.me.vip;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 商品信息
 */
public class ProductInfo {
    public String id;
    public String name;
    public String categoryId;
    public String brandId;
    public double originPrice;
    public double price;
    public String description;
    public String tag;
    public String payType;

    public ProductInfo parse(JSONObject obj) throws JSONException {
        this.id = obj.optString("id");
        this.name = obj.optString("name");
        this.categoryId = obj.optString("categoryId");
        this.brandId = obj.optString("brandId");
        this.originPrice = obj.optDouble("originPrice");
        this.price = obj.optDouble("price");
        this.description = obj.optString("description");
        boolean tagIsNull = obj.isNull("tag");
        if(!obj.isNull("tag")) {
            this.tag = obj.optString("tag");
        }
        this.payType = obj.optString("payType");


        return this;
    }
}
