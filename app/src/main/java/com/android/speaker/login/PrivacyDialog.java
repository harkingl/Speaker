package com.android.speaker.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.speaker.base.Constants;
import com.android.speaker.web.WebActivity;
import com.chinsion.SpeakEnglish.R;

public class PrivacyDialog extends Dialog implements View.OnClickListener{
    private TextView mContentTv;
    private TextView mAgreeBtn;
    private TextView mDisagreeBtn;
    private Context mContext;

    private DialogListener mListener;
    public PrivacyDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);

        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_privacy);
        mContentTv = findViewById(R.id.dialog_privacy_content_tv);
        mContentTv.setMovementMethod(new LinkMovementMethod());
        mAgreeBtn = findViewById(R.id.dialog_privacy_agree_tv);
        mDisagreeBtn = findViewById(R.id.dialog_privacy_disagree_tv);
        mAgreeBtn.setOnClickListener(this);
        mDisagreeBtn.setOnClickListener(this);

        initData();
    }

    private void initData() {
        String content = mContentTv.getText().toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(content);
        final int start = content.indexOf("《");//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                gotoWebPage(Constants.USER_AGREEMENT);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(mContext.getResources().getColor(R.color.common_purple_color));
                ds.setUnderlineText(false);
            }
        }, start, start + 6, 0);

        int end = content.lastIndexOf("《");
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                gotoWebPage(Constants.PRIVACY_PROTECTION);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(mContext.getResources().getColor(R.color.common_purple_color));
                ds.setUnderlineText(false);
            }
        }, end, end + 6, 0);

        mContentTv.setText(ssb);
    }

    private void gotoWebPage(String url) {
        Intent i = new Intent(mContext, WebActivity.class);
        i.putExtra("url", url);
        mContext.startActivity(i);
    }

    public void setListener(DialogListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.dialog_privacy_agree_tv:
                if(mListener != null) {
                    mListener.onAgree();
                }
                dismiss();
                break;
            case R.id.dialog_privacy_disagree_tv:
                if(mListener != null) {
                    mListener.onDisagree();
                }
                dismiss();
                break;
        }
    }

    public interface DialogListener {
        void onAgree();
        void onDisagree();
    }
}
