package com.android.speaker.me.vip;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/***
 * 会员信息
 */
public class GetVipInfoRequest extends BaseRequest<VipInfo> {
    public GetVipInfoRequest(Context context) {
        super(context);
    }
    @Override
    protected String url() {
        return UrlManager.GET_VIP_INFO;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected VipInfo result(JSONObject json) throws Exception {

        return new VipInfo().parse(json.optJSONObject("data"));
    }
}
