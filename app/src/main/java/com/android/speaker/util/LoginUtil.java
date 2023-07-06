package com.android.speaker.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.speaker.R;
import com.android.speaker.base.Constants;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.login.LoginCaptchaActivity;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.VerifyListener;

public class LoginUtil {
    private static final String TAG = "LoginUtil";

    public static void gotoLogin(BaseActivity activity) {
        // 如果未初始化成功，跳到验证码登陆页面
        if (!JVerificationInterface.isInitSuccess() || !JVerificationInterface.checkVerifyEnable(activity)) {
            activity.startActivity(new Intent(activity, LoginCaptchaActivity.class));
            return;
        }
        JVerificationInterface.setCustomUIWithConfig(getCustomUIConfig(activity));
        JVerificationInterface.loginAuth(activity, new VerifyListener() {
            @Override
            public void onResult(final int code, final String token, String operator) {
                LogUtil.d(TAG, "onResult: code=" + code + ",token=" + token + ",operator=" + operator);
                final String errorMsg = "operator=" + operator + ",code=" + code + "\ncontent=" + token;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mProgressbar.setVisibility(View.GONE);
//                        btnLoginDialog.setEnabled(true);
//                        btnLogin.setEnabled(true);
//                        if (code == Constants.CODE_LOGIN_SUCCESS) {
//                            toSuccessActivity(Constants.ACTION_LOGIN_SUCCESS, token);
//                            Log.e(TAG, "onResult: loginSuccess");
//                        } else if (code != Constants.CODE_LOGIN_CANCELD) {
//                            Log.e(TAG, "onResult: loginError");
//                            toFailedActivigy(code, token);
//                        }
//                    }
//                });
            }
        });
    }

    private static JVerifyUIConfig getCustomUIConfig(Context context){
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder();
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setLogoOffsetY(103);
        uiConfigBuilder.setNumFieldOffsetY(190);
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogoImgPath("ic_launcher");
        uiConfigBuilder.setNavTransparent(true);
        uiConfigBuilder.setNavReturnImgPath("ic_close");
        uiConfigBuilder.setCheckedImgPath("ic_checkbox_selected");
        uiConfigBuilder.setUncheckedImgPath("ic_checkbox_default");
        uiConfigBuilder.setNumberColor(context.getColor(R.color.text_color_1));
        uiConfigBuilder.setLogBtnImgPath("btn_bg_shape_purple");
        uiConfigBuilder.setLogBtnTextColor(0xFFFFFFFF);
        uiConfigBuilder.setLogBtnText("本机号码一键登录");
        uiConfigBuilder.setLogBtnOffsetY(255);
        uiConfigBuilder.setLogBtnWidth(300);
        uiConfigBuilder.setLogBtnHeight(45);
        uiConfigBuilder.setAppPrivacyColor(context.getColor(R.color.text_color_2),context.getColor(R.color.common_purple_color));
//        uiConfigBuilder.setPrivacyTopOffsetY(310);
        uiConfigBuilder.setPrivacyText("我已阅读并同意排课宝","");
        uiConfigBuilder.setPrivacyCheckboxHidden(false);
        uiConfigBuilder.setAppPrivacyOne("用户协议", Constants.USER_AGREEMENT);
//        uiConfigBuilder.setAppPrivacyTwo("隐私政策", Constants.PRIVACY_PROTECTION);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
        uiConfigBuilder.setPrivacyTextSize(11);
//        uiConfigBuilder.setPrivacyOffsetX(52-15);

        // 手机登录按钮
//        RelativeLayout.LayoutParams layoutParamPhoneLogin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParamPhoneLogin.setMargins(0, ScreenUtil.dip2px(360.0f),0,0);
//        layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
//        layoutParamPhoneLogin.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
//        TextView tvPhoneLogin = new TextView(context);
//        tvPhoneLogin.setText("手机号码登录");
//        tvPhoneLogin.setLayoutParams(layoutParamPhoneLogin);
//        uiConfigBuilder.addCustomView(tvPhoneLogin, false, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
////                toNativeVerifyActivity();
//            }
//        });

        LinearLayout layoutLoginGroup = new LinearLayout(context);
        RelativeLayout.LayoutParams layoutLoginGroupParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutLoginGroupParam.setMargins(0, ScreenUtil.dip2px(450.0f), 0, 0);
        layoutLoginGroupParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutLoginGroupParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutLoginGroupParam.setLayoutDirection(LinearLayout.HORIZONTAL);
        layoutLoginGroup.setLayoutParams(layoutLoginGroupParam);

        ImageView btnWechat = new ImageView(context);
        ImageView btnCaptcha = new ImageView(context);

        btnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JShareInterface.authorize(Wechat.Name, mAuthListener);
            }
        });
        btnCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LoginCaptchaActivity.class));
            }
        });

        btnWechat.setImageResource(R.drawable.ic_wx);
        btnCaptcha.setImageResource(R.drawable.ic_captcha);

        LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParam.setMargins(25,0,25,0);

        layoutLoginGroup.addView(btnCaptcha,btnParam);
        layoutLoginGroup.addView(btnWechat,btnParam);
        uiConfigBuilder.addCustomView(layoutLoginGroup, false, new JVerifyUIClickCallback() {
            @Override
            public void onClicked(Context context, View view) {
//                ToastUtil.showToast(MainActivity.this, "功能未实现", 1000);
            }
        });


//        final View dialogViewTitle = LayoutInflater.from(context).inflate(R.layout.dialog_login_title,null, false);
//
//        uiConfigBuilder.addNavControlView(dialogViewTitle, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
//
//            }
//        });

//        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_login_agreement,null, false);
//
//        dialogView.findViewById(R.id.dialog_login_no).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JVerificationInterface.dismissLoginAuthActivity();
//            }
//        });
//
//        dialogView.findViewById(R.id.dialog_login_yes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogView.setVisibility(View.GONE);
//                dialogViewTitle.setVisibility(View.GONE);
//            }
//        });
//
//
//        uiConfigBuilder.addCustomView(dialogView, false, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
////                ToastUtil.showToast(MainActivity.this, "功能未实现", 1000);
//            }
//        });


        return uiConfigBuilder.build();
    }
}
