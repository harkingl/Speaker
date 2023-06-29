package com.android.speaker.base.component;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.speaker.base.LoginTimeoutBroadcast;


public class BaseActivity extends AppCompatActivity {
    private LoginTimeoutBroadcast mLoginTimeoutBroadcast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        hideSoftInput();
        super.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLoginTimeoutBroadcast = new LoginTimeoutBroadcast(this);
        mLoginTimeoutBroadcast.registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mLoginTimeoutBroadcast != null) {
            mLoginTimeoutBroadcast.unregisterReceiver();
        }
    }

    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Window window = getWindow();
        if (window != null) {
            imm.hideSoftInputFromWindow(window.getDecorView().getWindowToken(), 0);
        }
    }
}
