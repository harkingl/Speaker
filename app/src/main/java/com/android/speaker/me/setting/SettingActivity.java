package com.android.speaker.me.setting;

import android.os.Bundle;
import android.view.View;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.chinsion.SpeakEnglish.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarLayout titleBarLayout;
    private View mSecurityLayout;
    private View mPersonalLayout;
    private View mHelpLayout;
    private View mAboutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
        setupViews();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.setting_title_bar);
        mSecurityLayout = findViewById(R.id.setting_security_ll);
        mPersonalLayout = findViewById(R.id.setting_personal_info_ll);
        mHelpLayout = findViewById(R.id.setting_help_ll);
        mAboutLayout = findViewById(R.id.setting_about_ll);

        mSecurityLayout.setOnClickListener(this);
        mPersonalLayout.setOnClickListener(this);
        mHelpLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
    }
    private void setupViews() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("设置", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mSecurityLayout) {
        } else if(v == mPersonalLayout) {
        } else if(v == mHelpLayout) {
        } else if(v == mAboutLayout) {
        }
    }
}