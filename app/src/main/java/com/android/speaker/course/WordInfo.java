package com.android.speaker.course;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 单词
 */
public class WordInfo implements Serializable {
    public String id;
    public String word;
    // 音标
    public String mark;
    public String desc;
    public WordExplain wordExplain;
    public String audioUrl;
    public boolean hasFav;
    public boolean isChecked;
    public boolean isPlaying;
    // 是否显示词义
    public boolean showExplain;

    public WordInfo parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        word = obj.optString("word");
        mark = obj.optString("mark");
        desc = obj.optString("desc");
        audioUrl = obj.optString("audioUrl");
        hasFav = obj.optBoolean("hasFav", false);
        JSONObject explainObj = obj.optJSONObject("wordExplain");
        if(explainObj != null) {
            wordExplain = new WordExplain();
            wordExplain.pos = explainObj.optString("pos");
            wordExplain.meaning = explainObj.optString("meaning");
        }

        return this;
    }

    public class WordExplain {
        // 词性
        public String pos;
        // 翻译
        public String meaning;
    }
}
