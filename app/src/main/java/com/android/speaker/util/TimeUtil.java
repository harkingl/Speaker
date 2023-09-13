package com.android.speaker.util;

import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Locale;

/***
 * 工具类
 */
public class TimeUtil {
    private static final String TAG = "TimeUtil";

    private static final String FORMAT_DURATION_TIME = "mm:ss";

    public static String formatTimeDuration(int duration) {
        return new SimpleDateFormat(FORMAT_DURATION_TIME).format(duration);
    }

    public static String timeToString(int time) {
        if(time <= 0) {
            return "00:00";
        }
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.getDefault());
        int seconds = time % 60;
        int minutes = (time/60) % 60;
        int hours = time/3600;
        if(hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
