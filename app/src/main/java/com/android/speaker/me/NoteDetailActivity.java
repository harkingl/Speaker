package com.android.speaker.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.server.okhttp.RequestListener;
import com.android.speaker.util.ToastUtil;
import com.chinsion.SpeakEnglish.R;

public class NoteDetailActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarLayout titleBarLayout;
    private TextView mTitleTv;
    private EditText mContentEt;
    private TextView mSaveTv;
    private NoteInfo mInfo;
    // 是否编辑
    private boolean mIsEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        initView();
        configActionbar();
        initData();
    }

    private void initView() {
        titleBarLayout = findViewById(R.id.note_detail_title_bar);
        mTitleTv = findViewById(R.id.note_detail_title_tv);
        mContentEt = findViewById(R.id.note_detail_content_et);
        mSaveTv = findViewById(R.id.note_detail_save_tv);

        mSaveTv.setOnClickListener(this);
    }
    private void configActionbar() {
        titleBarLayout.getRightIcon().setImageResource(R.drawable.ic_note_edit);
        titleBarLayout.setTitle("笔记本", ITitleBarLayout.Position.MIDDLE);
        titleBarLayout.setOnLeftClickListener(this);
        titleBarLayout.setOnRightClickListener(this);
    }

    private void initData() {
        mInfo = (NoteInfo) getIntent().getSerializableExtra("note_info");
        if(mInfo == null) {
            return;
        }
        mTitleTv.setText(mInfo.title);
        mContentEt.setText(mInfo.content);
    }

    @Override
    public void onClick(View v) {
        if(v == titleBarLayout.getLeftGroup()) {
            if(mIsEdit) {
                setResult(RESULT_OK);
            }
            finish();
        } else if(v == titleBarLayout.getRightGroup()) {
            onRightClick();
        } else if(v == mSaveTv) {
            doSave();
        }
    }

    @Override
    public void onBackPressed() {
        if(mIsEdit) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    private void onRightClick() {
        titleBarLayout.getRightIcon().setVisibility(View.GONE);
        mContentEt.setEnabled(true);
        mContentEt.requestFocus();
        mSaveTv.setVisibility(View.VISIBLE);
        String content = mContentEt.getText().toString();
        if(!TextUtils.isEmpty(content)) {
            mContentEt.setSelection(content.length());
        }
    }

    private void doSave() {
        if(mInfo == null) {
            return;
        }
        String content = mContentEt.getText().toString();
        if(TextUtils.isEmpty(content)) {
            ToastUtil.toastLongMessage("笔记内容不能为空");
            return;
        }
        mInfo.content = content;

        new UpdateNoteRequest(this, mInfo.id, content).schedule(true, new RequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mIsEdit = true;
                mContentEt.setEnabled(false);
                titleBarLayout.getRightIcon().setVisibility(View.VISIBLE);
                mSaveTv.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }
}