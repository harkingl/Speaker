package com.android.speaker.course;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.speaker.base.ITitleBarLayout;
import com.android.speaker.base.component.BaseActivity;
import com.android.speaker.base.component.NoScrollListView;
import com.android.speaker.base.component.TitleBarLayout;
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
    private String mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_report);

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
        String title = getIntent().getStringExtra(CourseUtil.KEY_TITLE);
        mTitleBarLayout.getRightIcon().setImageResource(R.drawable.ic_note_black);
        if(TextUtils.isEmpty(title)) {
            title = "对话报告";
        }
        mTitleBarLayout.setTitle(title, ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        List<String> pointList = new ArrayList<>();
        pointList.add("你完成了一次很棒的英语对话！接下来，让我们来总结一下对话中的一些常用表达，来强化我们的记忆吧。");
        pointList.add("当你电话的那端是你非常熟悉的人时，你可以用“Hey, it's me”来介绍你自己。电话常用的开始方式还有“Hey, What’s up?”");
        pointList.add("当你电话的那端是你非常熟悉的人时，你可以用“Hey, it's me”来介绍你自己。电话常用的开始方式还有“Hey, What’s up?”");
        pointList.add("当你电话的那端是你非常熟悉的人时，你可以用“Hey, it's me”来介绍你自己。电话常用的开始方式还有“Hey, What’s up?”");
        mPointLv.setAdapter(new ChatTipListAdapter(this, pointList));

        ArrayList<ChatItem> list = (ArrayList<ChatItem>) getIntent().getSerializableExtra(CourseUtil.KEY_CHAT_LIST);
        if(list != null && list.size() > 0) {
            mHistoryLv.setAdapter(new SpeakChatAdapter(this, list, true));
        }
        loadPointList();
    }

    private void loadPointList() {

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

    }
}
