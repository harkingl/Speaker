package com.android.speaker.me.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.speaker.base.Constants;
import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.web.WebActivity;
import com.chinsion.SpeakEnglish.R;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarLayout titleBarLayout;
    private TextView mVersionTv;
    private View mUserProtocolLayout;
    private View mPrivacyProtocolLayout;
    private View mVipProtocolLayout;
    private View mRenewalProtocolLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
        setupViews();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.about_title_bar);
        mVersionTv = findViewById(R.id.about_version_tv);
        mUserProtocolLayout = findViewById(R.id.about_user_protocol_ll);
        mPrivacyProtocolLayout = findViewById(R.id.about_privacy_protocol_ll);
        mVipProtocolLayout = findViewById(R.id.about_vip_protocol_ll);
        mRenewalProtocolLayout = findViewById(R.id.about_auto_renewal_protocol_ll);

        mUserProtocolLayout.setOnClickListener(this);
        mPrivacyProtocolLayout.setOnClickListener(this);
        mVipProtocolLayout.setOnClickListener(this);
        mRenewalProtocolLayout.setOnClickListener(this);
    }
    private void setupViews() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("关于", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);

        mVersionTv.setText(getVersion());
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mUserProtocolLayout) {
            gotoWebPage(Constants.USER_AGREEMENT);
        } else if(v == mPrivacyProtocolLayout) {
            gotoWebPage(Constants.PRIVACY_PROTECTION);
        } else if(v == mVipProtocolLayout) {
            gotoWebPage(Constants.VIP_SERVICE_PROTOCOL);
        } else if(v == mRenewalProtocolLayout) {
            gotoWebPage(Constants.RENEWAL_PROTOCOL);
        }
    }

    private void gotoWebPage(String url) {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    private String getVersion() {
        String version = "";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(),
                    0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
        }

        return version;
    }
}