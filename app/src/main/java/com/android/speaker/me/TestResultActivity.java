package com.android.speaker.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.chinsion.SpeakEnglish.R;

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
        LevelInfo info = (LevelInfo) getIntent().getSerializableExtra("level_result");
        if(info == null) {
            return;
        }
        mNameTv.setText(info.level + ". " + info.name);
        mDesTv.setText(info.levelDes);
    }

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup() || v == mFinishTv) {
            finish();
        }
    }
}
