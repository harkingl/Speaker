package com.android.speaker.course;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.base.component.TitleBarLayout;
import com.android.speaker.me.ChatReportInfo;
import com.android.speaker.me.NoteListActivity;
import com.chinsion.SpeakEnglish.R;

import java.util.ArrayList;
import java.util.List;

/***
 * 开口说对话报告
 */
public class ChatReportActivity extends BaseActivity implements View.OnClickListener {

    private TitleBarLayout mTitleBarLayout;
    private NoScrollListView mPointLv;
    private NoScrollListView mHistoryLv;
    private TextView mFinishTv;
    private ChatTipListAdapter mPointAdapter;
    private SpeakChatAdapter mHistoryAdapter;
    private ChatReportInfo mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_report);

        mInfo = (ChatReportInfo) getIntent().getSerializableExtra(CourseUtil.KEY_CHAT_REPORT_INFO);
        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.report_title_bar);
        mPointLv = findViewById(R.id.report_chat_point_lv);
        mHistoryLv = findViewById(R.id.report_chat_history_lv);
        mFinishTv = findViewById(R.id.report_finish_tv);

        mFinishTv.setOnClickListener(this);
    }

    private void configTitleBar() {
        String title = "对话报告";
        mTitleBarLayout.getRightIcon().setImageResource(R.drawable.ic_note_black);
        if(mInfo != null && !TextUtils.isEmpty(mInfo.title)) {
            title = mInfo.title;
        }
        mTitleBarLayout.setTitle(title, ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
        mTitleBarLayout.setOnRightClickListener(this);
    }

    private void initData() {
        if(mInfo == null) {
            return;
        }

        List<String> pointList = new ArrayList<>();
        if(mInfo.pointList != null) {
            pointList.addAll(mInfo.pointList);
        }
        mPointLv.setAdapter(new ChatTipListAdapter(this, pointList));

        if(mInfo.chatList != null && mInfo.chatList.size() > 0) {
            mHistoryLv.setAdapter(new SpeakChatAdapter(this, mInfo.chatList, true));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        } else if(v == mTitleBarLayout.getRightGroup()) {
            gotoNotePage();
        } else if(v == mFinishTv) {
            finish();
        }
    }

    private void gotoNotePage() {
        startActivity(new Intent(this, NoteListActivity.class));
    }
}
