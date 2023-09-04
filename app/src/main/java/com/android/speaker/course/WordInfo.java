package com.android.speaker.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单词
 */
public class WordInfo implements Serializable {
    public String id;
    public String word;
    public String desc;
    public WordExplain wordExplain;
    public String audioUrl;

    public WordInfo parse(JSONObject obj) throws JSONException {
        id = obj.optString("id");
        word = obj.optString("word");
        desc = obj.optString("desc");
        audioUrl = obj.optString("audioUrl");
        JSONObject explainObj = obj.optJSONObject("wordExplain");
        if(explainObj != null) {
            wordExplain = new WordExplain();
            JSONArray nArray = explainObj.optJSONArray("n");
            if(nArray != null) {
                wordExplain.n = new ArrayList<>();
                for(int i = 0; i < nArray.length(); i++) {
                    wordExplain.n.add(nArray.getString(i));
                }
            }
            JSONArray adjArray = explainObj.optJSONArray("adj");
            if(adjArray != null) {
                wordExplain.adj = new ArrayList<>();
                for(int i = 0; i < adjArray.length(); i++) {
                    wordExplain.adj.add(adjArray.getString(i));
                }
            }
            JSONArray advArray = explainObj.optJSONArray("adv");
            if(advArray != null) {
                wordExplain.adv = new ArrayList<>();
                for(int i = 0; i < advArray.length(); i++) {
                    wordExplain.adv.add(advArray.getString(i));
                }
            }
            JSONArray vArray = explainObj.optJSONArray("v");
            if(vArray != null) {
                wordExplain.v = new ArrayList<>();
                for(int i = 0; i < vArray.length(); i++) {
                    wordExplain.v.add(vArray.getString(i));
                }
            }
        }

        return this;
    }

    public class WordExplain {
        public List<String> n;
        public List<String> adj;
        public List<String> adv;
        public List<String> v;
    }
}
