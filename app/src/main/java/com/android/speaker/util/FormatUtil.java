package com.android.speaker.util;

import android.text.TextUtils;

/***
 * 工具类
 */
public class FormatUtil {
    private static final String TAG = "FormatUtil";

    /**
     * 手机号码格式化
     * @param tel
     * @return
     */
    public static String formatTelephone(String tel){
        if (TextUtils.isEmpty(tel)){
            return "";
        }
        if(tel.length() != 11) {
            return tel;
        }
        StringBuilder stringBuilder = new StringBuilder(tel);
        return stringBuilder.replace(3,8,"***").toString();
    }


    public static String formatIdCard(String identityCard){
        if (TextUtils.isEmpty(identityCard)||identityCard.length()!=18){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(identityCard);
        return stringBuilder.replace(3,14,"***********").toString();
    }

    public static boolean isContainStar(String in){
        if(TextUtils.isEmpty(in)){
            return false;
        }
        return in.contains("*");
    }
}
