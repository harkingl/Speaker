package com.android.speaker.me;

import android.content.Intent;
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
 * 等级测试
 */
public class LevelTestActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LevelTestActivity";

    private TitleBarLayout mTitleBarLayout;
    private ProgressBar mProgressBar;
    private TextView mCountTv;
    private TextView mWordTv;
    private FlowLayout mSelectFl;
    private List<LevelQuestionInfo> mList;
    private int mCurrIndex;
    // 当前item是否已经点击过
    private boolean mClicked = false;
    private Map<String, String> mAnswerMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_test_list);

        initView();
        configTitleBar();
        initData();
    }

    private void initView() {
        mTitleBarLayout = findViewById(R.id.level_test_title_bar);
        mProgressBar = findViewById(R.id.level_test_progress_bar);
        mCountTv = findViewById(R.id.level_test_count_tv);
        mWordTv = findViewById(R.id.level_test_word_tv);
        mSelectFl = findViewById(R.id.level_test_select_fl);
    }

    private void configTitleBar() {
        mTitleBarLayout.getRightIcon().setVisibility(View.GONE);
        mTitleBarLayout.setTitle("等级测试", ITitleBarLayout.Position.MIDDLE);
        mTitleBarLayout.setOnLeftClickListener(this);
    }

    private void initData() {
        mAnswerMap = new HashMap<>();

        new GetLevelTestListRequest(this).schedule(false, new RequestListener<List<LevelQuestionInfo>>() {
            @Override
            public void onSuccess(List<LevelQuestionInfo> result) {
                mList = result;
                if(result != null && result.size() > 0) {
                    setView(0);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void setView(int position) {
        if(position >= mList.size()) {
            queryResult();
            return;
        }
        LevelQuestionInfo info = mList.get(position);
        mProgressBar.setMax(mList.size());
        mProgressBar.setProgress(position+1);
        mCountTv.setText((position+1) + "/" + mList.size());
        mWordTv.setText(info.title);
        setTagView(info.selectList);

        mCurrIndex = position;
        mClicked = false;
    }

    private void queryResult() {
        new QueryTestResultRequest(this, mAnswerMap).schedule(true, new RequestListener<LevelInfo>() {
            @Override
            public void onSuccess(LevelInfo result) {
                gotoResultPage(result);
            }

            @Override
            public void onFailed(Throwable e) {
                ToastUtil.toastLongMessage(e.getMessage());
            }
        });
    }

    private void gotoResultPage(LevelInfo info) {
        Intent i = new Intent(this, TestResultActivity.class);
        i.putExtra("level_result", info);
        startActivity(i);

        finish();
    }

    private static final int WHAT_NEXT_QUESTION = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_NEXT_QUESTION:
                    setView(mCurrIndex+1);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        if(v == mTitleBarLayout.getLeftGroup()) {
            finish();
        }
    }

    private void setTagView(List<String> selectList) {
        if(selectList == null || selectList.size() == 0) {
            return;
        }
        mSelectFl.post(new Runnable() {
            @Override
            public void run() {
                mSelectFl.removeAllViews();
                int itemWidth = mSelectFl.getWidth()/3;
                for(String tagName : selectList) {
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(itemWidth, FlowLayout.LayoutParams.WRAP_CONTENT);
                    mSelectFl.addView(generateTagView(tagName), params);
                }
            }
        });
    }

    private View generateTagView(String tagName) {
        View v = LayoutInflater.from(this).inflate(R.layout.tag_select_question_item, null);
        TextView tv = v.findViewById(R.id.tag_name_tv);
        tv.setText(tagName);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClicked) {
                    return;
                }
                LevelQuestionInfo info = mList.get(mCurrIndex);
                mClicked = true;
                if(checkAnswer(info.answer, tagName)) {
                    tv.setBackgroundResource(R.drawable.green_bg_shape_5);
                    tv.setTextColor(getColor(R.color.white));
                } else {
                    tv.setBackgroundResource(R.drawable.red_bg_shape_5);
                    tv.setTextColor(getColor(R.color.white));
                }
                mAnswerMap.put(info.id, tagName);
                mHandler.sendEmptyMessageDelayed(WHAT_NEXT_QUESTION, 2000);
            }
        });

        return v;
    }

    private boolean checkAnswer(String answer, String selectAnswer) {
        if(TextUtils.isEmpty(selectAnswer) || TextUtils.isEmpty(answer)) {
            return false;
        }

        return answer.contains(selectAnswer);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(WHAT_NEXT_QUESTION);
        super.onDestroy();
    }
}
