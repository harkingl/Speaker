package com.android.speaker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.login.LoginCaptchaActivity;

public class LogOutUtil {
    public static void logout(Activity context) {
        UserInfo.getInstance().cleanUserInfo();
        gotoLoginActivity(context);
        if(!context.isFinishing()) {
            context.finish();
        }
    }

    private static void gotoLoginActivity(Context context) {
        context.startActivity(new Intent(context, LoginCaptchaActivity.class));
    }
}
