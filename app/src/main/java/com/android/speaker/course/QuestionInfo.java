package com.android.speaker.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionInfo implements Serializable {
    public static final int TYPE_FILL_BLANK = 0;
    public static final int TYPE_QUESTION_SELECT = 1;
    public static final int TYPE_LISTEN_SELECT = 2;
    public static final int TYPE_SPEAK = 3;
    public String id;
    public String sceneId;
    public String question;
    public List<String> questionSelect;
    public List<String> answerList;
    // 0填空，1选择
    public int type;
    public double showTime;
    // 语音url
    public String audioUrl;
    // 答案解析
    public String answerAnalysis;
    // 翻译
    public String translation;

    public QuestionInfo parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        sceneId = obj.optString("sceneId");
        question = obj.optString("question");
        JSONArray array = obj.optJSONArray("questionSelects");
        if(array != null) {
            questionSelect = new ArrayList<>();
            for(int i = 0; i < array.length(); i++) {
                questionSelect.add(array.getString(i));
            }
        }
        JSONArray answerArray = obj.optJSONArray("answers");
        if(answerArray != null) {
            answerList = new ArrayList<>();
            for(int i = 0; i < answerArray.length(); i++) {
                answerList.add(answerArray.getString(i));
            }
        }
        type = obj.optInt("type");
        showTime = obj.optDouble("showTime");
        audioUrl = obj.optString("audioOssKey");
        answerAnalysis = obj.optString("answerAnalysis");
        translation = obj.optString("translation");

        return this;
    }
}
