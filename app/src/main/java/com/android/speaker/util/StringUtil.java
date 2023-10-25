package com.android.speaker.util;

import android.text.TextUtils;

public class StringUtil {
    private static final String TAG = "StringUtil";

    public static String unescapeString(String text) {
        if(TextUtils.isEmpty(text)) {
            return "";
        }

        return text.replaceAll("\\\\(.)", "$1");
    }
}
