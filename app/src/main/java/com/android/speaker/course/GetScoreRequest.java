package com.android.speaker.course;

import android.content.Context;

import com.android.speaker.server.okhttp.BaseFileRequest;
import com.android.speaker.server.okhttp.BaseRequest;
import com.android.speaker.server.util.UrlManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/***
 * 获取单词评分
 */
public class GetScoreRequest extends BaseFileRequest<String> {
    private File file;
    private String word;
    public GetScoreRequest(Context context, File file, String word) {
        super(context);
        this.file = file;
        this.word = word;
    }
    @Override
    protected String url() {
        return UrlManager.GET_SCORE;
    }

    @Override
    protected String body()  throws JSONException {
        JSONObject obj = new JSONObject();
        return obj.toString();
    }

    @Override
    protected String result(JSONObject json) throws Exception {

        return json.optString("data");
    }

    @Override
    protected File fileBody() throws JSONException {
        return file;
    }

    @Override
    protected String uploadDataKey() {
        return "world";
    }

    @Override
    protected String uploadData() throws JSONException {
        return word;
    }

    @Override
    protected String mediaType() {
        return "application/octet-stream";
    }
}
