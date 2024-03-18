package com.android.speaker.update;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class VersionInfo implements Serializable {
    public String versionName;
    public String updateUrl;
    public boolean isNew;

    public VersionInfo parse(JSONObject obj) throws JSONException {
        versionName = obj.optString("versionName");
        updateUrl = obj.optString("updateUrl");

        isNew = obj.optBoolean("isNew", false);

        return this;
    }
}
