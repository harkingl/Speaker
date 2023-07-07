package com.android.speaker.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.speaker.R;
import com.android.speaker.base.bean.UserInfo;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.home.HomeActivity;
import com.android.speaker.util.LoginUtil;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.text_color_FCFCFC));
        }
        setContentView(R.layout.activity_splash);

        handleData();
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

//            startActivity(new Intent(SplashActivity.this, LoginOneKeyActivity.class));
//            finish();
            LoginUtil.gotoLogin(SplashActivity.this);
        }
    };

    private void handleData() {
        UserInfo userInfo = UserInfo.getInstance();
//        if (userInfo != null && !TextUtils.isEmpty(userInfo.getToken())) {
//            AppConfig.DEMO_SDK_APPID = GenerateTestUserSig.SDKAPPID;
//            // 自动登录im sdk
//            login();
//        } else {
//            startLogin();
//        }
    }
}
