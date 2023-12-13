package com.android.speaker.me;

import android.content.Context;

import com.android.speaker.me.vip.VipInfo;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 更新笔记
 */
public class UpdateNoteRequest extends BaseRequest<Boolean> {
    private String id;
    private String content;
    public UpdateNoteRequest(Context context, String id, String content) {
        super(context);
        this.id = id;
        this.content = content;
    }
    @Override
    protected String url() {
        return UrlManager.UPDATE_NOTE_INFO + "/" + id;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("content", content);
        return obj.toString();
    }

    @Override
    protected Boolean result(JSONObject json) throws Exception {

        return true;
    }
}
