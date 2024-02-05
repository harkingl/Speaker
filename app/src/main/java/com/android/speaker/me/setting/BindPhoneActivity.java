package com.android.speaker.me.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.login.GetCaptchaRequest;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 绑定手机号页面
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BindPhoneActivity.class.getSimpleName();

    private static final int DEFAULT_CAPTCHA_COUNT = 60;

    private ImageView mCloseIv;
    private EditText mPhoneEt;
    private EditText mCaptchaEt;
    private TextView mBtnCaptchaTv;
    private TextView mBindTv;

    private Timer mTimer;
    private SendCodeTimerTask mTimerTask;
    private int mSendCodeCount = DEFAULT_CAPTCHA_COUNT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivity();
    }

    private void initActivity() {
        setContentView(R.layout.activity_bind_phone);

        mPhoneEt = findViewById(R.id.bind_phone_et);
        mCaptchaEt = findViewById(R.id.bind_captcha_et);
        mBtnCaptchaTv = findViewById(R.id.bind_captcha_send_tv);
        mBindTv = findViewById(R.id.bind_btn_tv);

        mBtnCaptchaTv.setOnClickListener(this);
        mBindTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.bind_captcha_send_tv:
                sendCaptcha();
                break;
            case R.id.bind_btn_tv:
                doBind();
                break;
        }
    }

    private void sendCaptcha() {
        String phone = mPhoneEt.getText().toString();
        if(TextUtils.isEmpty(phone)) {
            ToastUtil.toastLongMessage("请输入手机号");
            return;
        }
        if(phone.length() != 11) {
            ToastUtil.toastLongMessage("手机号格式不正确");
            return;
        }
        new GetCaptchaRequest(this, phone).schedule(true, new RequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                startTimer();
            }

            @Override
            public void onFailed(Throwable e) {
                LogUtil.e(TAG, "Get captcha failed：" + e.getMessage());
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void doBind() {
        String phone = mPhoneEt.getText().toString();
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

        new BindPhoneRequest(this, phone, captcha).schedule(true, new RequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                UserInfo info = UserInfo.getInstance();
                info.setPhone(phone);
                info.storeUserInfo();
                ToastUtil.toastLongMessage("绑定成功");

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });

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

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
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
