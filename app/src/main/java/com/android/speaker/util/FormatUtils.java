package com.android.speaker.util;

import android.text.TextUtils;

import java.text.DecimalFormat;

/***
 * 工具类
 */
public class FormatUtils {
    private static final String TAG = "FormatUtils";

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

    private static DecimalFormat format = new DecimalFormat("#.##");

    public static String formatPrice(int value) {
        double price = value * 1.0 / 100;
        return format.format(price);
    }

    public static String formatPrice(long value) {
        double price = value * 1.0 / 100 ;
        return format.format(price) ;
    }
}
