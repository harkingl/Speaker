package com.android.speaker.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.R;
import com.android.speaker.base.Constants;
import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.util.ToastUtil;
import com.android.speaker.web.WebActivity;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 验证码登陆页面
 */
public class LoginCaptchaActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = LoginCaptchaActivity.class.getSimpleName();

    private static final int DEFAULT_CAPTCHA_COUNT = 60;

    private TextView mCloseTv;
    private EditText mAccountEt;
    private EditText mCaptchaEt;
    private TextView mBtnCaptchaTv;
    private TextView mLoginTv;
    private CheckBox mProtocolCb;
    private TextView mServiceProtocolTv;
    private TextView mPrivateProtocolTv;

    private Timer mTimer;
    private SendCodeTimerTask mTimerTask;
    private int mSendCodeCount = DEFAULT_CAPTCHA_COUNT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivity();
    }

    private void initActivity() {
        setContentView(R.layout.activity_login_captcha);

        mCloseTv = findViewById(R.id.login_captcha_close_tv);
        mAccountEt = findViewById(R.id.login_captcha_phone_et);
        mCaptchaEt = findViewById(R.id.login_captcha_et);
        mBtnCaptchaTv = findViewById(R.id.login_captcha_send_tv);
        mLoginTv = findViewById(R.id.login_captcha_login_btn_tv);
        mProtocolCb = findViewById(R.id.login_captcha_protocol_cb);
        mServiceProtocolTv = findViewById(R.id.login_captcha_service_protocol_tv);
        mPrivateProtocolTv = findViewById(R.id.login_captcha_private_protocol_tv);

        mCloseTv.setOnClickListener(this);
        mBtnCaptchaTv.setOnClickListener(this);
        mLoginTv.setOnClickListener(this);
        mServiceProtocolTv.setOnClickListener(this);
        mPrivateProtocolTv.setOnClickListener(this);

        String phone = UserInfo.getInstance().getPhone();
        if(!TextUtils.isEmpty(phone)) {
            mAccountEt.setText(phone);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.login_captcha_close_tv:
                finish();
                break;
            case R.id.login_captcha_send_tv:
                sendCaptcha();
                break;
            case R.id.login_captcha_login_btn_tv:
                doLogin();
                break;
            case R.id.login_captcha_service_protocol_tv:
                gotoServiceProtocol();
                break;
            case R.id.login_captcha_private_protocol_tv:
                gotoPrivateProtocol();
                break;
        }
    }

    private void sendCaptcha() {
        String phone = mAccountEt.getText().toString();
        if(TextUtils.isEmpty(phone)) {
            ToastUtil.toastLongMessage("请输入手机号");
            return;
        }
        if(phone.length() != 11) {
            ToastUtil.toastLongMessage("手机号格式不正确");
            return;
        }
//        new GetCaptchaRequest(this, phone, GetCaptchaRequest.TYPE_LOGIN).schedule(true, new RequestListener<Boolean>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                startTimer();
//            }
//
//            @Override
//            public void onFailed(Throwable e) {
//                Log.e(TAG, "Get captcha failed：" + e.getMessage());
//                ToastUtil.toastLongMessage(e.getMessage());
//            }
//        });
    }

    private void doLogin() {
        String phone = mAccountEt.getText().toString();
        String captcha = mCaptchaEt.getText().toString();
        if(TextUtils.isEmpty(phone)) {
            ToastUtil.toastLongMessage("请输入手机号");
            return;
        }
        if(phone.length() != 11) {
            ToastUtil.toastLongMessage("手机号格式不正确");
            return;
        }
        if(TextUtils.isEmpty(captcha)) {
            ToastUtil.toastLongMessage("请输入验证码");
            return;
        }
        if(!mProtocolCb.isChecked()) {
            ToastUtil.toastLongMessage("请先同意隐私政策和服务协议");
            return;
        }

//        ThreadUtils.execute(new LoginTask(this, captcha, phone, "", LoginRequest.TYPE_CAPTCHA, new RequestListener<UserInfo>() {
//            @Override
//            public void onSuccess(UserInfo result) {
//                ToastUtil.toastLongMessage("登录成功");
//                loginSuccess(result);
//            }
//
//            @Override
//            public void onFailed(Throwable e) {
//                Log.e(TAG, "Login failed：" + e.getMessage());
//                ToastUtil.toastLongMessage(e.getMessage());
//
//            }
//        }));
    }

    private void gotoServiceProtocol() {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra("url", Constants.USER_AGREEMENT);
        startActivity(i);
    }

    private void gotoPrivateProtocol() {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra("url", Constants.PRIVACY_PROTECTION);
        startActivity(i);
    }

    private void loginSuccess(UserInfo info) {

    }

    // 开始倒计时
    private void startTimer() {
        stopTimer();
        mTimer = new Timer();
        mTimerTask = new SendCodeTimerTask();
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void stopTimer() {
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private class SendCodeTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new MyRunnable());
        }
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            if (mSendCodeCount == 0) {
                mBtnCaptchaTv
                        .setText(getString(R.string.get_captcha));
                mBtnCaptchaTv.setTextColor(getColor(R.color.common_purple_color));
                mBtnCaptchaTv.setEnabled(true);
                mSendCodeCount = DEFAULT_CAPTCHA_COUNT;
                stopTimer();
            } else if (mSendCodeCount == DEFAULT_CAPTCHA_COUNT) {
                mBtnCaptchaTv.setEnabled(false);
                mBtnCaptchaTv.setText(getString(
                        R.string.captcha_count_down,
                        mSendCodeCount + ""));
                mBtnCaptchaTv.setTextColor(getColor(R.color.text_color_CACACA));
                mSendCodeCount--;
            } else {
                mBtnCaptchaTv.setText(getString(
                        R.string.captcha_count_down,
                        mSendCodeCount + ""));
                mBtnCaptchaTv.setTextColor(getColor(R.color.text_color_CACACA));
                mSendCodeCount--;
            }
        }
    }
}
