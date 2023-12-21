package com.android.speaker.me.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.speaker.base.Constants;
import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.util.ToastUtil;
import com.android.speaker.web.WebActivity;
import com.chinsion.SpeakEnglish.R;

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarLayout titleBarLayout;
    private EditText mContentEt;
    private TextView mCountTv;
    private EditText mPhoneEt;
    private TextView mCommitTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        initView();
        setupViews();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.help_title_bar);
        mContentEt = findViewById(R.id.help_content_et);
        mCountTv = findViewById(R.id.help_count_tv);
        mPhoneEt = findViewById(R.id.help_phone_et);
        mCommitTv = findViewById(R.id.help_commit_tv);

        mCommitTv.setOnClickListener(this);
    }
    private void setupViews() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("帮助与反馈", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);

        mContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCountTv.setText(s.toString().length() + "/200");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mCommitTv) {
            doCommit();
        }
    }

    private void doCommit() {
        String content = mContentEt.getText().toString();
        String phone = mPhoneEt.getText().toString();
        if(TextUtils.isEmpty(content)) {
            ToastUtil.toastLongMessage("请输入反馈内容");
            return;
        }
        if(TextUtils.isEmpty(phone)) {
            ToastUtil.toastLongMessage("请输入您的联系方式");
            return;
        }
    }
}