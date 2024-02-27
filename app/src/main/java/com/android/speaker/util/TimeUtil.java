package com.android.speaker.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/***
 * 工具类
 */
public class TimeUtil {
    private static final String TAG = "TimeUtil";

    private static final String FORMAT_DURATION_TIME = "mm:ss";
    public static final String FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

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

    /**
     * dateFormat
     * @param timestamp 单位ms
     * @return
     */
    public static String getStringFromDate(String dateFormat, long timestamp) {
        if(TextUtils.isEmpty(dateFormat) || timestamp <= 0) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(new Date(timestamp));
    }

    /**
     * 获取有效期时间（跟当前系统时间比较）
     * @param time
     * @param format
     * @return 时间差（ms）
     */
    public static long getValidateTime(String time, String format) {
        if(TextUtils.isEmpty(time) || TextUtils.isEmpty(format)) {
            return 0;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(time).getTime() - System.currentTimeMillis();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

        return 0;
    }
}
