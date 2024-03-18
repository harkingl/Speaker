package com.android.speaker.update;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 检查版本更新
 */
public class CheckVersionRequest extends BaseRequest<VersionInfo> {
    private String version;
    public CheckVersionRequest(Context context, String version) {
        super(context);

        this.version = version;
    }
    @Override
    protected String url() {
        return UrlManager.CHECK_VERSION;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("version", version);
        return obj.toString();
    }

    @Override
    protected VersionInfo result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        VersionInfo info = new VersionInfo();
        if(data != null) {
            info.parse(data);
        }
        return info;
    }
}
