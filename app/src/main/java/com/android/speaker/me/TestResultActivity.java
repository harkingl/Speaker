package com.android.speaker.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

import org.apmem.tools.layouts.FlowLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 测试结果
 */
public class TestResultActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TestResultActivity";

    private TitleBarLayout mTitleBarLayout;
    private TextView mNameTv;
    private TextView mDesTv;
    private TextView mFinishTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.test_result_title_bar);
        mNameTv = findViewById(R.id.test_result_level_name_tv);
        mDesTv = findViewById(R.id.test_result_level_des_tv);
        mFinishTv = findViewById(R.id.test_result_finish_tv);

        mFinishTv.setOnClickListener(this);
    }

    private void configTitleBar() {
        mTitleBarLayout.getRightIcon().setVisibility(View.GONE);
        mTitleBarLayout.setTitle("我的等级", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        mNameTv.setText("L3. 初级");
        mDesTv.setText("能讨论更多生活话题，描述自己的想法");
    }

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup() || v == mFinishTv) {
            finish();
        }
    }
}
