package com.android.speaker.me;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/***
 * 订单信息
 */
public class OrderInfo implements Serializable {
    public String product;
    public String productDetails;
    public double originalPrice;
    public double actualPayment;
    public String orderNumber;
    public String orderDate;
    public String productType;
    public int count;

    public OrderInfo parse(JSONObject obj) throws JSONException {
        this.product = obj.optString("product");
        this.productDetails = obj.optString("productDetails");
        this.originalPrice = obj.optDouble("originalPrice");
        this.actualPayment = obj.optDouble("actualPayment");
        this.orderNumber = obj.optString("orderNumber");
        this.orderDate = obj.optString("orderDate");
        this.productType = obj.optString("productType");
        this.count = obj.optInt("count");

        return this;
    }
}
