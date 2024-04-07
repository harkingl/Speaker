package com.android.speaker.util;

import static com.android.speaker.util.ThreadUtils.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.speaker.base.Constants;
import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.home.HomeActivity;
import com.android.speaker.login.LoginCaptchaActivity;
import com.android.speaker.login.LoginTask;
import com.android.speaker.server.okhttp.RequestListener;
import com.chinsion.SpeakEnglish.R;

import cn.jiguang.share.android.api.AuthListener;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.model.AccessTokenInfo;
import cn.jiguang.share.android.model.BaseResponseInfo;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.VerifyListener;

public class LoginUtil {
    private static final String TAG = "LoginUtil";

    private static LoginUtil mInstance;
    private BaseActivity mContext;

    private LoginUtil(BaseActivity context) {
        this.mContext = context;
    }

    public static LoginUtil getInstance(BaseActivity context) {
        if(mInstance == null) {
            mInstance = new LoginUtil(context);
        }

        return mInstance;
    }

    /**
     * 一键登录
     * @param activity
     */
    public void oneKeyLogin(BaseActivity activity) {
        // 如果未初始化成功，跳到验证码登陆页面
        if (!JVerificationInterface.isInitSuccess() || !JVerificationInterface.checkVerifyEnable(activity)) {
            gotoCaptchaLoginPage();
            return;
        }
        JVerificationInterface.setCustomUIWithConfig(getCustomUIConfig(activity));
        JVerificationInterface.loginAuth(activity, new VerifyListener() {
            @Override
            public void onResult(final int code, final String token, String operator) {
                LogUtil.d(TAG, "onResult: code=" + code + ",token=" + token + ",operator=" + operator);
                final String errorMsg = "operator=" + operator + ",code=" + code + "\ncontent=" + token;
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == 6000) {
                            doLogin(token, "666666", "", "mobile");
                            LogUtil.d(TAG, "onResult: loginSuccess");
                        } else if(code != 6002) {// dismissLoginAuthActivity会触发取消回调
                            LogUtil.d(TAG, "onResult: loginError");
                            gotoCaptchaLoginPage();
                        }
                    }
                });
            }
        });
    }

    private void doLogin(String token, String extId, String openId, String grant_type) {
        ThreadUtils.execute(new LoginTask(mContext, token, extId, openId, grant_type, mListener));
    }

    private RequestListener<UserInfo> mListener = new RequestListener<UserInfo>() {
        @Override
        public void onSuccess(UserInfo result) {
            ToastUtil.toastLongMessage("登录成功");
            gotoHomePage();
            mContext.finish();
        }

        @Override
        public void onFailed(Throwable e) {
            ToastUtil.toastLongMessage(e.getMessage());
            LogUtil.e(TAG, "Login failed：" + e.getMessage());
        }
    };

    private void gotoHomePage() {
        Intent i = new Intent(mContext, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(i);
    }

    private void gotoCaptchaLoginPage() {
        JVerificationInterface.dismissLoginAuthActivity();
        mContext.startActivity(new Intent(mContext, LoginCaptchaActivity.class));
    }

    private JVerifyUIConfig getCustomUIConfig(Context context){
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
        uiConfigBuilder.setPrivacyText("我已阅读并同意开口说", "、", "、", "");
        uiConfigBuilder.setPrivacyCheckboxHidden(false);
//        uiConfigBuilder.setPrivacyCheckboxSize(ScreenUtil.dip2px(5));
        uiConfigBuilder.setAppPrivacyOne("用户协议", Constants.USER_AGREEMENT);
        uiConfigBuilder.setAppPrivacyTwo("隐私政策", Constants.PRIVACY_PROTECTION);
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
                wxAuthLogin();
            }
        });
        btnCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCaptchaLoginPage();
            }
        });

        btnWechat.setImageResource(R.drawable.ic_wx);
        btnCaptcha.setImageResource(R.drawable.ic_captcha);

        LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParam.setMargins(ScreenUtil.dip2px(14),0,ScreenUtil.dip2px(14),0);

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

    /**
     * 微信授权登录
     */
    public void wxAuthLogin() {
        JShareInterface.authorize(Wechat.Name, mAuthListener);
    }

    private AuthListener mAuthListener = new AuthListener() {
        @Override
        public void onComplete(Platform platform, int action, BaseResponseInfo data) {
            LogUtil.d(TAG, "onComplete:" + platform + ",action:" + action + ",data:" + data);
            String toastMsg = null;
            switch (action) {
                case Platform.ACTION_AUTHORIZING:
                    if (data instanceof AccessTokenInfo) {        //授权信息
                        JVerificationInterface.dismissLoginAuthActivity();
                        String token = ((AccessTokenInfo) data).getToken();//token
                        long expiration = ((AccessTokenInfo) data).getExpiresIn();//token有效时间，时间戳
                        String refresh_token = ((AccessTokenInfo) data).getRefeshToken();//refresh_token
                        String openid = ((AccessTokenInfo) data).getOpenid();//openid
                        //授权原始数据，开发者可自行处理
                        String originData = data.getOriginData();
                        toastMsg = "授权成功:" + data.toString();
                        LogUtil.d(TAG, "openid:" + openid + ",token:" + token + ",expiration:" + expiration + ",refresh_token:" + refresh_token);
                        LogUtil.d(TAG, "originData:" + originData);
//                        toSuccessActivity(Constants.ACTION_THIRD_AUTHORIZED_SUCCESS, token);
                        doLogin(token, "", openid, "wechat");
                    }
                    break;
            }
            JShareInterface.removeAuthorize(platform.getName(),null);
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            LogUtil.d(TAG, "onError:" + platform + ",action:" + action + ",error:" + error);
            switch (action) {
                case Platform.ACTION_AUTHORIZING:
                    JVerificationInterface.dismissLoginAuthActivity();
                    LogUtil.d(TAG, "onResult: loginError:"+errorCode);
//                    toFailedActivityThird(errorCode, "授权失败" + (error != null ? error.getMessage() : "") + "---" + errorCode);
                    break;
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            LogUtil.d(TAG, "onCancel:" + platform + ",action:" + action);
            String toastMsg = null;
            switch (action) {
                case Platform.ACTION_AUTHORIZING:
                    toastMsg = "取消授权";
                    break;
            }
        }
    };
}
