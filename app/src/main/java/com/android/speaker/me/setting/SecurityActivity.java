package com.android.speaker.me.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.LogOutUtil;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import cn.jiguang.share.android.api.AuthListener;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.model.AccessTokenInfo;
import cn.jiguang.share.android.model.BaseResponseInfo;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.verifysdk.api.JVerificationInterface;

public class SecurityActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SecurityActivity.class.getSimpleName();

    private static final int REQ_BIND_PHONE = 111;

    private TitleBarLayout titleBarLayout;
    private View mPhoneLayout;
    private TextView mPhoneBindTv;
    private View mThirdLoginLayout;
    private ImageView mThirdLoginIv;
    private View mLogoffLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        initView();
        configActionbar();
        setupViews();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.security_title_bar);
        mPhoneLayout = findViewById(R.id.security_phone_ll);
        mPhoneBindTv = findViewById(R.id.security_phone_bind_tv);
        mThirdLoginLayout = findViewById(R.id.security_third_login_ll);
        mThirdLoginIv = findViewById(R.id.security_third_login_iv);
        mLogoffLayout = findViewById(R.id.security_logoff_ll);

//        mPhoneLayout.setOnClickListener(this);
//        mThirdLoginLayout.setOnClickListener(this);
        mLogoffLayout.setOnClickListener(this);
    }

    private void configActionbar() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("账号与安全", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
    }

    private void setupViews() {
        UserInfo userInfo = UserInfo.getInstance();
        if(TextUtils.isEmpty(userInfo.getPhone())) {
            mPhoneBindTv.setText("未绑定");
            mPhoneLayout.setOnClickListener(this);
        } else {
            mPhoneBindTv.setText(userInfo.getPhone());
            mPhoneLayout.setOnClickListener(null);
        }
        if(userInfo.isWx()) {
            mThirdLoginIv.setImageResource(R.drawable.ic_wx_logined);
            mThirdLoginLayout.setOnClickListener(null);
        } else {
            mThirdLoginIv.setImageResource(R.drawable.ic_wx_unlogin);
            mThirdLoginLayout.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mPhoneLayout) {
            gotoBindPage();
        } else if(v == mThirdLoginLayout) {
            JShareInterface.authorize(Wechat.Name, mAuthListener);
        } else if(v == mLogoffLayout) {
            showLogOffDialog(this);
        }
    }

    private void gotoBindPage() {
        startActivityForResult(new Intent(this, BindPhoneActivity.class), REQ_BIND_PHONE);
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
                        bindWx(token, openid);
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

    private void bindWx(String accessToken, String openId) {
        new BindWxRequest(this, accessToken, openId).schedule(true, new RequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                UserInfo info = UserInfo.getInstance();
                info.setWx(true);
                info.storeUserInfo();
                setupViews();
                ToastUtil.toastLongMessage("绑定成功");
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void showLogOffDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_log_off, null);
        final AlertDialog dialog = builder.create();
        dialog.setView(layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_log_off);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        TextView applyTv = window.findViewById(R.id.dialog_btn_apply_tv);
        applyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView thinkTv = window.findViewById(R.id.dialog_btn_think_tv);
        thinkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void doApply() {
        // log out请求
        LogOutUtil.logout(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQ_BIND_PHONE && resultCode == RESULT_OK) {
            setupViews();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}