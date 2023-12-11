package com.android.speaker.me.vip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 优惠券列表集合
 */
public class CouponListEntity {
    public static final int TYPE_NOT_USE = 0;
    public static final int TYPE_HAVED_USE = 1;
    public static final int TYPE_EXIPRE_USE = 2;
    public List<CouponInfo> notUseList;
    public List<CouponInfo> havedUseList;
    public List<CouponInfo> exipreUseList;

    public CouponListEntity parse(JSONObject obj) throws JSONException {
        notUseList = new ArrayList<>();
        JSONArray notUseArray = obj.optJSONArray("notUse");
        if(notUseArray != null) {
            for(int i = 0; i < notUseArray.length(); i++) {
                this.notUseList.add(new CouponInfo().parse(notUseArray.getJSONObject(i)));
            }
        }

        havedUseList = new ArrayList<>();
        JSONArray havedUseArray = obj.optJSONArray("havedUse");
        if(havedUseArray != null) {
            for(int i = 0; i < havedUseArray.length(); i++) {
                this.havedUseList.add(new CouponInfo().parse(havedUseArray.getJSONObject(i)));
            }
        }

        exipreUseList = new ArrayList<>();
        JSONArray exipreUseArray = obj.optJSONArray("exipreUse");
        if(exipreUseArray != null) {
            for(int i = 0; i < exipreUseArray.length(); i++) {
                this.exipreUseList.add(new CouponInfo().parse(exipreUseArray.getJSONObject(i)));
            }
        }

        return this;
    }
}
