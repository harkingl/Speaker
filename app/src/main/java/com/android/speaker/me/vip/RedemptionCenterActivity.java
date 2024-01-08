package com.android.speaker.me.vip;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

/***
 * 兑换中心
 */
public class RedemptionCenterActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarLayout titleBarLayout;
    private EditText mCodeEt;
    private TextView mBtnTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption_center);

        initView();
        setupViews();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.redemption_title_bar);
        mCodeEt = findViewById(R.id.redemption_code_et);
        mBtnTv = findViewById(R.id.redemption_btn_tv);

        mBtnTv.setOnClickListener(this);
    }
    private void setupViews() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        titleBarLayout.setTitle("兑换中心", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mBtnTv) {
            doRedemption();
        }
    }

    private void doRedemption() {
        String code = mCodeEt.getText().toString();
        if(TextUtils.isEmpty(code)) {
            ToastUtil.toastLongMessage("请输入兑换码");
            return;
        }
    }
}