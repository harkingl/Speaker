package com.android.speaker.util;

import android.text.TextUtils;
import android.util.Log;

import com.chinsion.SpeakEnglish.BuildConfig;

public class LogUtil {
    public static void i(String tag, String message) {
        if(BuildConfig.DEBUG && !TextUtils.isEmpty(message)) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if(BuildConfig.DEBUG && !TextUtils.isEmpty(message)) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(BuildConfig.DEBUG && !TextUtils.isEmpty(message)) {
            Log.e(tag, message);
        }
    }
}
