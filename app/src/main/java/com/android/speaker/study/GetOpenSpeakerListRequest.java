package com.android.speaker.study;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetOpenSpeakerListRequest extends BaseRequest<List<OpenSpeakerInfo>> {
    private int pageNum;
    private int pageSize;
    public GetOpenSpeakerListRequest(Context context, int pageNum, int pageSize) {
        super(context);

        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    @Override
    protected String url() {
        return UrlManager.GET_OPEN_SPEAKER_LIST;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pageNum", pageNum);
        obj.put("pageSize", pageSize);
        return obj.toString();
    }

    @Override
    protected List<OpenSpeakerInfo> result(JSONObject json) throws Exception {
        JSONObject data = json.optJSONObject("data");
        JSONArray array = data.optJSONArray("list");
        List<OpenSpeakerInfo> list = new ArrayList<>();

        if(array != null && array.length() > 0) {
            for(int i = 0; i < array.length(); i++) {
                list.add(new OpenSpeakerInfo().parse(array.getJSONObject(i)));
            }
        }
        return list;
    }
}
